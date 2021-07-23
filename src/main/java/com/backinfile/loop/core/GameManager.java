package com.backinfile.loop.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.backinfile.loop.Res;
import com.backinfile.loop.actor.WorldView;
import com.backinfile.loop.core.Cube.CubeType;
import com.backinfile.loop.support.Utils;

public class GameManager {
	public static final GameManager instance = new GameManager();

	public WorldData worldData;
	public WorldView worldView;
	private Cube human;
	private Cube trans;

	private LinkedList<History> histories = new LinkedList<History>();

	private static final int[] dx = new int[] { 0, 0, -1, 1 };
	private static final int[] dy = new int[] { 1, -1, 0, 0 };

	public void init() {
		worldData = WorldData.parse(Res.getDefaultWorldConf());

		worldData.actualMap.forEach((pos, cube) -> {
			if (cube.type == CubeType.Human) {
				human = cube;
			} else if (cube.type == CubeType.Trans) {
				trans = cube;
			}
		});
	}

	public void resetGame() {
		worldData.actualMap.forEach((pos, cube) -> {
			cube.resetPosition();
		});
		histories.clear();
	}

	public void undo() {
		if (!histories.isEmpty()) {
			History history = histories.pollLast();
			history.playback();
		}
	}

	public WorldData getWorldData() {
		return worldData;
	}

	public Pos getWorldSize() {
		return new Pos(Res.CUBE_SIZE * worldData.baseWidth, Res.CUBE_SIZE * worldData.baseWidth);
	}
	
	private boolean moveCube(Pos curPos, Pos d, ArrayList<Pos> passPosList) {
		while (true) {
			Pos nextPos = getNextPos(curPos, d);
			if (nextPos != null && passPosList.contains(nextPos)) { // 循环达成， 不可以移动
				return false;
			}
			if (nextPos == null || isPosStop(nextPos)) { // 碰到墙了
				// 尝试找到路径中的分型
				for (Integer index : getSplitByTransPosList(passPosList)) {
					Pos transPos = passPosList.get(index);

					ArrayList<Pos> newPassPosList = Utils.subList(passPosList, 0, index);
					Pos edgeEmptyPos = getEdgeEmptyPos(d);
					if (edgeEmptyPos != null) { // 移动或推动尝试进入分型，没有卡在墙边
						newPassPosList.add(edgeEmptyPos);
						if (isPosEmpty(edgeEmptyPos)) {
							newPassPosList.add(edgeEmptyPos);
							_moveCubePosList(newPassPosList);
							// 顺利移动或者推动进入方块
							return true;
						} else {
							if (moveCube(edgeEmptyPos, d, new ArrayList<>(newPassPosList))) {
								// 移动或推动尝试进入方块 成功
								return true;
							}
						}
					}
					// 尝试吞掉方块
					if (index < passPosList.size() - 1) {
						edgeEmptyPos = getEdgeEmptyPos(d.getOppsite());
						if (edgeEmptyPos != null) {
							newPassPosList.add(transPos);
							newPassPosList.add(passPosList.get(index + 1));
							newPassPosList.add(edgeEmptyPos);
							if (isPosEmpty(edgeEmptyPos)) { // 分型内部为空，直接移动过去
								_moveCubePosList(newPassPosList);
								return true;
							}
							if (moveCube(edgeEmptyPos, d.getOppsite(), new ArrayList<Pos>(newPassPosList))) {
								// 分型内部可以移动
								return true;
							}
						}
					}
				}
				// 推动墙，失败
				return false;
			}
			if (isPosEmpty(nextPos)) {
				passPosList.add(nextPos);
				_moveCubePosList(passPosList);
				// 顺利移动或者推动
				return true;
			}
			passPosList.add(nextPos);
			curPos = nextPos;
		}
	}

	private List<Integer> getSplitByTransPosList(List<Pos> posList) {
		List<Integer> indexList = new ArrayList<>();
		for (int i = posList.size() - 1; i >= 0; i--) {
			if (isPosTrans(posList.get(i))) {
				indexList.add(i);
			}
		}
		return indexList;
	}

	private void _moveCubePosList(ArrayList<Pos> posList) {
		if (posList.isEmpty()) {
			return;
		}

		// 记录操作
		List<Cube> cubes = new ArrayList<>();
		for (Pos pos : posList) {
			Cube cube = worldData.actualMap.get(pos);
			if (cube != null) {
				cubes.add(cube);
			}
		}
		if (!cubes.isEmpty()) {
			histories.addLast(History.getHistory(cubes));
		}

		// 进行移动
		for (int i = 0; i < posList.size() - 1; i++) {
			posList.get(i).set(posList.get(i + 1));
		}
	}

	public void moveHuman(int dura) {
		Pos d = new Pos(dx[dura], dy[dura]);
		Pos curPos = human.pos;
		ArrayList<Pos> passPosList = new ArrayList<>();
		passPosList.add(curPos);
		moveCube(curPos, d, passPosList);
	}

	private Pos getEdgeEmptyPos(Pos d) {
		if (d.x == 0) {
			for (int x = 0; x < worldData.baseWidth; x++) {
				Pos pos = new Pos(x, d.y < 0 ? worldData.baseHeight - 1 : 0);
				if (isPosEmpty(pos)) {
					return pos;
				}
			}
			for (int x = 0; x < worldData.baseWidth; x++) {
				Pos pos = new Pos(x, d.y < 0 ? worldData.baseHeight - 1 : 0);
				if (isPosPushable(pos)) {
					return worldData.actualMap.get(pos).pos;
				}
			}
		} else {
			for (int y = 0; y < worldData.baseHeight; y++) {
				Pos pos = new Pos(d.x < 0 ? worldData.baseWidth - 1 : 0, y);
				if (isPosEmpty(pos)) {
					return pos;
				}
			}
			for (int y = 0; y < worldData.baseHeight; y++) {
				Pos pos = new Pos(d.x < 0 ? worldData.baseWidth - 1 : 0, y);
				if (isPosPushable(pos)) {
					return worldData.actualMap.get(pos).pos;
				}
			}
		}
		return null;
	}

	private Pos getNextPos(Pos curPos, Pos d) {
		Pos nextPos = curPos.getTransPos(d);
		if (!worldData.actualMap.isFit(nextPos)) {
			nextPos = trans.pos.getTransPos(d);
			if (!worldData.actualMap.isFit(nextPos)) {
				return null;
			}
		}
		Cube cube = worldData.actualMap.get(nextPos);
		if (cube != null) {
			return cube.pos;
		} else {
			return nextPos;
		}
	}

	private void move(Pos pos, Pos d) {
		Cube cube = worldData.actualMap.get(pos);
		cube.pos.trans(d);
	}

	private boolean isPosEmpty(Pos pos) {
		Cube cube = worldData.actualMap.get(pos);
		return cube == null;
	}

	private boolean isPosPushable(Pos pos) {
		Cube cube = worldData.actualMap.get(pos);
		if (cube == null) {
			return false;
		}
		return cube.type == CubeType.Rock || cube.type == CubeType.Trans || cube.type == CubeType.Human;
	}

	private boolean isPosTrans(Pos pos) {
		Cube cube = worldData.actualMap.get(pos);
		if (cube == null) {
			return false;
		}
		return cube.type == CubeType.Trans;
	}

	private boolean isPosStop(Pos pos) {
		Cube cube = worldData.actualMap.get(pos);
		if (cube == null) {
			return false;
		}
		return cube.type == CubeType.Wall;
	}

}

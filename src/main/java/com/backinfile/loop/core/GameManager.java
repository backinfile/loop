package com.backinfile.loop.core;

import java.util.ArrayList;

import com.backinfile.loop.Res;
import com.backinfile.loop.actor.WorldView;
import com.backinfile.loop.core.Cube.CubeType;

public class GameManager {
	public static final GameManager instance = new GameManager();

	public WorldData worldData;
	public WorldView worldView;
	private Cube human;
	private Cube trans;

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

	public WorldData getWorldData() {
		return worldData;
	}

	private boolean moveCube(Pos curPos, Pos d, ArrayList<Pos> passPosList) {
		main: while (true) {
			Pos nextPos = getNextPos(curPos, d);
			if (nextPos != null && passPosList.contains(nextPos)) {
				// 循环达成， 不可以移动
				return false;
			}
			if (nextPos == null || isPosStop(nextPos)) {
				for (int i = passPosList.size() - 1; i >= 0; i--) {
					if (isPosTrans(passPosList.get(i))) {
						Pos edgeEmptyPos = getEdgeEmptyPos(d);
						if (edgeEmptyPos == null) {
							// 移动或推动尝试进入方块，但卡在墙边
							return false;
						}
						passPosList = new ArrayList<Pos>(passPosList.subList(0, i));
						passPosList.add(edgeEmptyPos);

						if (isPosEmpty(edgeEmptyPos)) {
							passPosList.add(edgeEmptyPos);
							_moveCubePosList(passPosList);
							// 顺利移动或者推动进入方块
							return true;
						} else {
							// 移动或推动尝试进入方块
							curPos = edgeEmptyPos;
							continue main;
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

	private void _moveCubePosList(ArrayList<Pos> posList) {
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

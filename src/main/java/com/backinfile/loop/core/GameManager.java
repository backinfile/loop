package com.backinfile.loop.core;

import com.backinfile.loop.Res;
import com.backinfile.loop.actor.WorldView;
import com.backinfile.loop.core.Cube.CubeType;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameManager {
	public static final GameManager instance = new GameManager();

	public WorldData worldData;
	public WorldView worldView;
	private Cube human;

	private static final int[] dx = new int[] { 0, 0, -1, 1 };
	private static final int[] dy = new int[] { 1, -1, 0, 0 };

	public void init() {
		worldData = WorldData.parse(Res.getDefaultWorldConf());

		worldData.actualMap.forEach((pos, cube) -> {
			if (cube.type == CubeType.Human) {
				human = cube;
			}
		});
	}

	public WorldData getWorldData() {
		return worldData;
	}

	public void moveHuman(int dura) {
		human.pos.x += dx[dura];
		human.pos.y += dy[dura];
	}

}

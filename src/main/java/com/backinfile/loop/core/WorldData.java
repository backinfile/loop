package com.backinfile.loop.core;

import java.util.ArrayList;
import java.util.List;

import com.backinfile.loop.core.Cube.CubeType;

public class WorldData {
	public int baseWidth;
	public int baseHeight;
	public int shapeWidth;
	public int shapeHeight;
	public MMap<Cube> actualMap;
	public List<Cube> transCubeList = new ArrayList<>();
	public List<Pos> winPosList = new ArrayList<Pos>();
	public Pos humanWinPos;

	public static WorldData parse(String worldConf) {
		WorldData world = new WorldData();
		String[] confs = worldConf.split("\n");
		for (int index = 0; index < confs.length; index++) {
			String curLine = confs[index].trim();
			if (curLine.startsWith("size=")) {
				String[] sizeConf = curLine.substring(5).split("\\*");
				if (sizeConf.length == 2) {
					world.baseWidth = Integer.valueOf(sizeConf[0]);
					world.baseHeight = Integer.valueOf(sizeConf[1]);
					world.actualMap = new MMap<Cube>(world.baseWidth, world.baseHeight);
				}
			} else if (curLine.startsWith("out=")) {
				String[] posConf = curLine.substring(4).split(",");
				if (posConf.length % 2 == 0) {
					if (posConf.length > 0) {
						world.humanWinPos = new Pos(Integer.valueOf(posConf[0]), Integer.valueOf(posConf[1]));
					}
				}
				for (int i = 1; i < posConf.length / 2; i++) {
					Pos pos = new Pos(Integer.valueOf(posConf[i * 2]), Integer.valueOf(posConf[i * 2 + 1]));
					world.winPosList.add(pos);
				}
			} else if (curLine.startsWith("map=")) {
				for (int h = 0; h < world.baseHeight; h++) {
					String line = confs[index + h + 1];
					for (int w = 0; w < world.baseWidth; w++) {
						Cube cube = null;
						switch (line.charAt(w)) {
						case 'w':
							cube = new Cube(CubeType.Wall);
							break;
						case 't':
							cube = new Cube(CubeType.Trans);
							break;
						case 'm':
							cube = new Cube(CubeType.Human);
							break;
						case 'b':
							cube = new Cube(CubeType.Rock);
							break;
						default:
							break;
						}
						if (cube != null) {
							cube.setPosition(w, world.baseHeight - h - 1);
							cube.setOriPosition(w, world.baseHeight - h - 1);
							world.actualMap.add(cube);
						}
					}
				}
				index += world.baseHeight + 1;
			}
		}
		return world;
	}
}

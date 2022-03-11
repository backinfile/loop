package com.backinfile.loop.core;

import com.backinfile.loop.Log;
import com.backinfile.loop.core.Cube.CubeType;

public class WorldData {
    public String name;
    public int baseWidth;
    public int baseHeight;
    public MMap<Cube> actualMap;
    public MMap<Cube> floorMap;

    public static WorldData parse(String worldConf) {
        WorldData world = new WorldData();
        String[] confLines = worldConf.split("\n");
        for (int index = 0; index < confLines.length; index++) {
            String curLine = confLines[index].trim();
            if (curLine.startsWith("name=")) {
                if (world.name != null) {
                    break;
                }
                world.name = curLine.substring(5);
                Log.game.info("start read map {}", world.name);
            } else if (curLine.startsWith("size=")) {
                String[] sizeConf = curLine.substring(5).split("\\*");
                if (sizeConf.length == 2) {
                    world.baseWidth = Integer.parseInt(sizeConf[0]);
                    world.baseHeight = Integer.parseInt(sizeConf[1]);
                    world.actualMap = new MMap<>(world.baseWidth, world.baseHeight);
                    world.floorMap = new MMap<>(world.baseWidth, world.baseHeight);
                    Log.game.info("read map size={}*{}", world.baseWidth, world.baseHeight);
                }
            } else if (curLine.startsWith("out=")) {
                String[] winPosConf = curLine.substring(4).split(",");
                for (int i = 0; i < winPosConf.length / 2; i++) {
                    Pos pos = new Pos(Integer.parseInt(winPosConf[i * 2]), Integer.parseInt(winPosConf[i * 2 + 1]));
                    Cube cube = new Cube(CubeType.WinPos);
                    cube.setPosition(pos.x, world.baseHeight - pos.y - 1);
                    cube.setOriPosition(pos.x, world.baseHeight - pos.y - 1);
                    world.floorMap.add(cube);
                }
                Log.game.info("read win pos size:{}", world.floorMap.size());
            } else if (curLine.startsWith("map=")) {
                for (int h = 0; h < world.baseHeight; h++) {
                    String line = confLines[index + h + 1];
                    for (int w = 0; w < world.baseWidth && w < line.length(); w++) {
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
                Log.game.info("read cube size={}", world.actualMap.size());
            }
        }
        return world;
    }
}

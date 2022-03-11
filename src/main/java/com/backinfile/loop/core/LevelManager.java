package com.backinfile.loop.core;

import com.backinfile.loop.Log;
import com.backinfile.loop.Res;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LevelManager {
    public static final LevelManager instance = new LevelManager();

    private final List<String> levelNameList = new ArrayList<>();
    private final HashMap<String, WorldData> worldDataMap = new HashMap<>();
    private int curLevelIndex = -1;

    public void init() {
        String text = Res.getDefaultWorldConf();
        for (String line : text.split("\n")) {
            if (line.startsWith("levels=")) {
                String[] split = line.substring(7).trim().split(",");
                levelNameList.addAll(Arrays.asList(split));
                break;
            }
        }
        for (String worldText : text.split("\n(?=name=)")) {
            WorldData worldData = WorldData.parse(worldText);
            if (worldData.name != null && levelNameList.contains(worldData.name)) {
                worldDataMap.put(worldData.name, worldData);
                Log.game.info("add level {}", worldData.name);
            }
        }
    }

    public void nextLevel() {
        curLevelIndex = Math.min(curLevelIndex + 1, levelNameList.size() - 1);
        String levelName = levelNameList.get(curLevelIndex);
        WorldData worldData = worldDataMap.get(levelName);
        GameManager.instance.initLevel(worldData);
    }
}

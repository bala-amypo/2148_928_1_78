package com.example.demo.util;

import java.util.Map;

public class SkillLevelUtil {
    
    private static final Map<String, Integer> LEVEL_RANKS = Map.of(
        "BEGINNER", 1,
        "INTERMEDIATE", 2,
        "EXPERT", 3
    );
    
    private static final Map<String, Integer> PRIORITY_RANKS = Map.of(
        "LOW", 1,
        "MEDIUM", 2,
        "HIGH", 3
    );
    
    public static int levelRank(String level) {
        return LEVEL_RANKS.getOrDefault(level, 0);
    }
    
    public static int priorityRank(String priority) {
        return PRIORITY_RANKS.getOrDefault(priority, 0);
    }
}
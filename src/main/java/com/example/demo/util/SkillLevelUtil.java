package com.example.demo.util;

public class SkillLevelUtil {

    public static int levelRank(String level) {
        if (level == null) return 0;
        return switch (level) {
            case "BEGINNER" -> 1;
            case "INTERMEDIATE" -> 2;
            case "EXPERT" -> 3;
            default -> 0;
        };
    }

    public static int priorityRank(String priority) {
        if (priority == null) return 0;
        return switch (priority) {
            case "LOW" -> 1;
            case "MEDIUM" -> 2;
            case "HIGH" -> 3;
            default -> 0;
        };
    }
}

package com.codenjoy.dojo.chess.model;

public enum Color {
    WHITE(0),
    BLACK(1);

    private final int priority;

    Color(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public static Color byPriority(int priority) {
        for (Color color : Color.values()) {
            if (color.priority == priority) {
                return color;
            }
        }
        return null;
    }

    public static Color withHighestPriority() {
        return WHITE;
    }
}

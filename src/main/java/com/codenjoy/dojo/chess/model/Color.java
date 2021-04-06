package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.services.Direction;

public enum Color {
    WHITE(0, Direction.UP),
    BLACK(1, Direction.DOWN);

    private final int priority;
    private final Direction attackDirection;

    Color(int priority, Direction attackDirection) {
        this.priority = priority;
        this.attackDirection = attackDirection;
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

    public Direction getAttackDirection() {
        return attackDirection;
    }
}

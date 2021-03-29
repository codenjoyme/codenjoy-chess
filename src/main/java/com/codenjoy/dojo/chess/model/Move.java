package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class Move {

    private final Point from;
    private final Point to;

    private Move(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public static Builder from(int x, int y) {
        return from(new PointImpl(x, y));
    }

    public static Builder from(Point from) {
        return new Builder(from);
    }

    public int[] command() {
        return new int[]{from.getX(), from.getY(), to.getX(), to.getY()};
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }

    public static class Builder {
        private final Point from;

        private Builder(Point from) {
            this.from = from;
        }

        public Move to(int x, int y) {
            return to(new PointImpl(x, y));
        }

        public Move to(Point to) {
            return new Move(from, to);
        }
    }
}

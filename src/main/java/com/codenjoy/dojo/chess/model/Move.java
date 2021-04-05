package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.PieceType;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class Move {
    private final Point from;
    private final Point to;

    private PieceType promotion;

    private Move(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public static Move decode(int... p) {
        if (p.length < 4 || p.length > 5) {
            throw new IllegalArgumentException();
        }
        Move move = Move.from(p[0], p[1]).to(p[2], p[3]);
        if (p.length == 5) {
            move.promotion(p[4]);
        }
        return move;
    }

    public static Builder from(int x, int y) {
        return from(new PointImpl(x, y));
    }

    public static Builder from(Point from) {
        return new Builder(from);
    }

    public int[] command() {
        return promotion == null
                ? new int[]{from.getX(), from.getY(), to.getX(), to.getY()}
                : new int[]{from.getX(), from.getY(), to.getX(), to.getY(), promotion.getId()};
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }

    public Move promotion(PieceType piece) {
        promotion = piece;
        return this;
    }

    public Move promotion(int pieceId) {
        return promotion(PieceType.byId(pieceId));
    }

    public boolean withPromotion() {
        return promotion != null;
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

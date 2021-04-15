package com.codenjoy.dojo.chess.engine.service;

import com.codenjoy.dojo.chess.engine.model.Color;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

@SuppressWarnings("SuspiciousNameCombination")
public class PositionMapper {
    private final int boardSize;

    public PositionMapper(int boardSize) {
        this.boardSize = boardSize;
    }

    public static int countRotationTimes(Direction from, Direction to) {
        int times = 0;
        Direction direction = from;
        while (direction != to) {
            direction = direction.clockwise();
            times++;
        }
        return times;
    }

    public <T extends Point> void mapPosition(Color colorFrom, Color colorTo, T position) {
        rotateClockwise(
                position,
                countRotationTimes(colorFrom.getAttackDirection(), colorTo.getAttackDirection())
        );
    }

    public <T extends Point> void mapPosition(Color color, T position) {
        rotateClockwise(position, countRotationTimes(color.getAttackDirection(), Direction.UP));
    }

    public void mapPosition(Color color, Iterable<? extends Point> positions) {
        positions.forEach(p -> mapPosition(color, p));
    }

    public Move mapMove(Color color, Move move) {
        Point from = move.getFrom().copy();
        Point to = move.getTo().copy();
        mapPosition(Color.WHITE, color, from);
        mapPosition(Color.WHITE, color, to);
        return Move.from(from).to(to).promotion(move.getPromotion());
    }

    /**
     * Rotates position clockwise.
     *
     * @param position the position to be rotated
     * @param times how much times to rotate
     */
    public <T extends Point> void rotateClockwise(T position, int times) {
        if ((times = times % 4) == 0) {
            return;
        }
        int x = position.getX();
        int y = position.getY();
        if (times == 1) {
            position.move(y, boardSize - 1 - x);
        } else if (times == 2) {
            position.move(boardSize - 1 - x, boardSize - 1 - y);
        } else {
            position.move(boardSize - 1 - y, x);
        }
    }
}

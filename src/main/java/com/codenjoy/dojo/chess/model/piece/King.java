package com.codenjoy.dojo.chess.model.piece;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.GameBoard;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.services.Direction.*;

public class King extends Piece {

    public King(Color color, GameBoard board, Point position) {
        super(Type.KING, color, board, position);
    }

    public void move(Point position) {
        board.getPieceAt(position).ifPresent(p -> p.setAlive(false));
        this.position = position;
        moved = true;
    }

    @Override
    public List<Move> getAvailableMoves() {
        List<Move> moves = listOfAvailableMoves(
                LEFT.change(position),
                UP.change(position),
                RIGHT.change(position),
                DOWN.change(position),
                LEFT.change(UP.change(position)),
                UP.change(RIGHT.change(position)),
                RIGHT.change(DOWN.change(position)),
                DOWN.change(LEFT.change(position))
        );
        if (!moved) {
            Point point = position;
            do {
                point = getAttackDirection().counterClockwise().change(point);
            } while (board.isInBounds(point) && (board.getPieceAt(point).isEmpty() || (board.getPieceAt(point).get().getType() != Type.ROOK && board.getPieceAt(point).get().getColor() != color)));
            if (board.getPieceAt(point).isPresent()) {
                Piece rook = board.getPieceAt(point).get();
                Direction direction = defineDirection(position, rook.getPosition());
                Point rookPosition = direction.change(position);
                Point kingPosition = direction.change(rookPosition);
                if (!board.isUnderAttack(rookPosition, color) && !board.isUnderAttack(kingPosition, color)) {
                    if (rook.getColor() == color && !rook.isMoved()) {
                        moves.add(Move.from(position).to(point));
                    }
                }
            }
            point = position;
            do {
                point = getAttackDirection().clockwise().change(point);
            } while (board.isInBounds(point) && (board.getPieceAt(point).isEmpty() || (board.getPieceAt(point).get().getType() != Type.ROOK && board.getPieceAt(point).get().getColor() != color)));
            if (board.getPieceAt(point).isPresent()) {
                Piece rook = board.getPieceAt(point).get();
                if (rook.getColor() == color && !rook.isMoved()) {
                    moves.add(Move.from(position).to(point));
                }
            }
        }
        return moves;
    }

    private Direction defineDirection(Point from, Point to) {
        if (from.equals(to)) {
            return null;
        }
        if (from.getX() == to.getX()) {
            return from.getY() < to.getY() ? Direction.UP : Direction.DOWN;
        }
        if (from.getY() == to.getY()) {
            return from.getX() < to.getX() ? Direction.RIGHT : Direction.LEFT;
        }
        return null;
    }

    private List<Move> listOfAvailableMoves(Point... destinations) {
        List<Move> result = Lists.newArrayList();
        for (Point dest : destinations) {
            if (isAvailable(dest)) {
                result.add(Move.from(position).to(dest));
            }
        }
        return result;
    }

    private boolean isAvailable(Point dest) {
        Optional<Piece> pieceAtDest = board.getPieceAt(dest);
        return board.isInBounds(dest) && (pieceAtDest.isEmpty() || pieceAtDest.get().getColor() != color);
    }

    @Override
    public void setAlive(boolean alive) {
        if (this.alive && !alive) {
            this.alive = false;
            board.die(color);
        } else {
            this.alive = alive;
        }
    }
}

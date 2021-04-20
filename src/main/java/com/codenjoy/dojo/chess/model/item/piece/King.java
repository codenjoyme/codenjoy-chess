package com.codenjoy.dojo.chess.model.item.piece;

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
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.service.GameBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.services.Direction.*;

// https://en.wikipedia.org/wiki/King_(chess)
public class King extends Piece {

    public King(Color color, GameBoard board, Point position) {
        super(Type.KING, color, board, position);
    }

    public static List<Move> availableMoves(GameBoard board,
                                            Point position,
                                            Color color,
                                            boolean moved,
                                            boolean withCastling,
                                            Direction attackDirection
    ) {
        List<Move> moves = filterAvailableMoves(board, position, color,
                LEFT.change(position),
                UP.change(position),
                RIGHT.change(position),
                DOWN.change(position),
                LEFT.change(UP.change(position)),
                UP.change(RIGHT.change(position)),
                RIGHT.change(DOWN.change(position)),
                DOWN.change(LEFT.change(position))
        );
        if (withCastling && !moved && !board.isUnderAttack(position, color)) {
            moves.addAll(castlingMoves(board, position, color, attackDirection));
        }
        return moves;
    }

    // TODO check behaviour when on custom board start positions of king and rook next to each other
    public static List<Move> castlingMoves(GameBoard board,
                                           Point position,
                                           Color color,
                                           Direction attackDirection) {
        List<Move> castlingMoves = Lists.newArrayList();
        getCastling(board, position, color, attackDirection.clockwise())
                .ifPresent(castlingMoves::add);
        getCastling(board, position, color, attackDirection.counterClockwise())
                .ifPresent(castlingMoves::add);
        return castlingMoves;
    }

    private static Optional<Move> getCastling(GameBoard board,
                                              Point kingPosition,
                                              Color color,
                                              Direction direction) {
        Point rookPosition = kingPosition;
        do {
            rookPosition = direction.change(rookPosition);
        } while (board.isInBounds(rookPosition) && board.getPieceAt(rookPosition).isEmpty());

        Piece rook;
        if (board.getPieceAt(rookPosition).isEmpty()
                || (rook = board.getPieceAt(rookPosition).get()).getType() != Type.ROOK
                || rook.isMoved()
                || rook.getColor() != color) {
            return Optional.empty();
        }

        Point newRookPosition = direction.change(kingPosition);
        Point newKingPosition = direction.change(newRookPosition);
        if (board.isUnderAttack(newRookPosition, color) || board.isUnderAttack(newKingPosition, color)) {
            return Optional.empty();
        }

        return Optional.ofNullable(Move.from(kingPosition).to(rookPosition));
    }

    private static List<Move> filterAvailableMoves(GameBoard board, Point position, Color color, Point... destinations) {
        List<Move> result = Lists.newArrayList();
        for (Point dest : destinations) {
            if (isAvailable(board, dest, color)) {
                result.add(Move.from(position).to(dest));
            }
        }
        return result;
    }

    private static boolean isAvailable(GameBoard board, Point dest, Color color) {
        Optional<Piece> pieceAtDest = board.getPieceAt(dest);
        return board.isInBounds(dest) && (pieceAtDest.isEmpty() || pieceAtDest.get().getColor() != color);
    }

    @Override
    public List<Move> getAvailableMoves() {
        return availableMoves(board, position, color, moved, true, attackDirection);
    }

    @Override
    public boolean isAttacks(Point position) {
        return availableMoves(board, this.position, color, moved, false, attackDirection).stream()
                .map(Move::getTo)
                .anyMatch(p -> p.equals(position));
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
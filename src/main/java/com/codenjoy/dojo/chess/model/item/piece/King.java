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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.QDirection.*;

// https://en.wikipedia.org/wiki/King_(chess)
public class King extends Piece {

    public King(Color color, GameBoard board, Point position) {
        super(Type.KING, color, board, position);
    }

    /**
     * The method calculates all available moves of king piece
     * in described circumstances, including castling moves (optionally)
     * and those where enemy's piece can be taken.
     *
     * @param board           a chess board
     * @param position        a position of a king
     * @param color           a color of the king
     * @param moved           if the king already moved at least once or not
     * @param withCastling    get castling moves if available or not
     * @param attackDirection direction of attack of the king
     * @return all available moves according to parameters
     */
    public static List<Move> availableMoves(GameBoard board,
                                            Point position,
                                            Color color,
                                            boolean moved,
                                            boolean withCastling,
                                            Direction attackDirection
    ) {
        List<Move> moves = Stream.of(
                LEFT.change(position),
                UP.change(position),
                RIGHT.change(position),
                DOWN.change(position),
                LEFT_UP.change(position),
                LEFT_DOWN.change(position),
                RIGHT_UP.change(position),
                RIGHT_DOWN.change(position)
        )
                .filter(board::isInBounds)
                .filter(dest -> isFreeOrWithEnemy(board, dest, color))
                .map(dest -> Move.from(position).to(dest))
                .collect(Collectors.toList());

        if (withCastling && !moved && !board.isUnderAttack(position, color)) {
            moves.addAll(castlingMoves(board, position, color, attackDirection));
        }
        return moves;
    }

    /**
     * The method calculates all castling moves of king piece
     * in described circumstances.
     *
     * @param board           a chess board
     * @param position        a position of a king
     * @param color           a color of the king
     * @param attackDirection direction of attack of the king
     * @return all available castling moves
     */
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

    /**
     * The method calculates castling move in specific direction if it is possible.
     *
     * @param board        a chess board
     * @param kingPosition a position of a king
     * @param color        a color of the king
     * @param direction    castling direction
     * @return castling move if it is possible, Optional.empty() otherwise
     */
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

    private static boolean isFreeOrWithEnemy(GameBoard board, Point dest, Color color) {
        Optional<Piece> pieceAtDest = board.getPieceAt(dest);
        return pieceAtDest.isEmpty() || pieceAtDest.get().getColor() != color;
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
}
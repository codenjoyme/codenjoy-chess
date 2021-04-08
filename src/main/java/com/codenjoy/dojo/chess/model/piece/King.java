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


import com.codenjoy.dojo.chess.model.Board;
import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.services.Direction.*;

public class King extends Piece {

    public King(Color color, Board board, Point position) {
        super(PieceType.KING, color, board, position);
    }

    public void move(Point position) {
        board.getAt(position).ifPresent(p -> p.setAlive(false));
        this.position = position;
        moved = true;
    }

    @Override
    public List<Move> getAvailableMoves() {
        List<Move> moves = listOfAvailableMoves(board,
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
                point = LEFT.change(point);
            } while (board.isInBounds(point) && (board.getAt(point).isEmpty() || (board.getAt(point).get().getType() != PieceType.ROOK && board.getAt(point).get().getColor() != color)));
            if (board.getAt(point).isPresent()) {
                Piece rook = board.getAt(point).get();
                if (!rook.isMoved()) {
                    moves.add(Move.from(position).to(point));
                }
            }
            point = position;
            do {
                point = RIGHT.change(point);
            } while (board.isInBounds(point) && (board.getAt(point).isEmpty() || (board.getAt(point).get().getType() != PieceType.ROOK && board.getAt(point).get().getColor() != color)));
            if (board.getAt(point).isPresent()) {
                Piece rook = board.getAt(point).get();
                if (!rook.isMoved()) {
                    moves.add(Move.from(position).to(point));
                }
            }
        }
        return moves;
    }

    private List<Move> listOfAvailableMoves(Board board, Point... destinations) {
        List<Move> result = Lists.newArrayList();
        for (Point dest : destinations) {
            if (isAvailable(board, dest)) {
                result.add(Move.from(position).to(dest));
            }
        }
        return result;
    }

    private boolean isAvailable(Board board, Point dest) {
        Optional<Piece> pieceAtDest = board.getAt(dest);
        return board.isInBounds(dest) && (pieceAtDest.isEmpty() || pieceAtDest.get().getColor() != color);
    }

    @Override
    public void setAlive(boolean value) {
        this.alive = value;
        if (!alive) {
            board.getPieces(color).stream()
                    .filter(Piece::isAlive)
                    .forEach(p -> p.setAlive(false));
        }
    }
}

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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Knight extends Piece {

    public Knight(Color color, GameBoard board, Point position) {
        super(Type.KNIGHT, color, board, position);
    }

    /**
     * Checks if destination is available to move to or not,
     * including a cases when there is enemy's piece at destination square,
     * or destination point is out of bounds of chess board.
     *
     * @param board       a chess board
     * @param destination the destination point
     * @param color       a color of a knight
     * @return true, if destination is available to move to, false otherwise
     */
    private static boolean isAvailable(GameBoard board, Point destination, Color color) {
        return board.getPieceAt(destination)
                .map(p -> p.getColor() != color)
                .orElse(board.isInBounds(destination));
    }

    /**
     * The method calculates all available moves of knight piece
     * in described circumstances, including those
     * where enemy's piece can be taken.
     *
     * @param board    a chess board
     * @param position a position of a knight
     * @param color    a color of the knight
     * @return all available moves
     */
    private static List<Move> availableMoves(GameBoard board, Point position, Color color) {
        int x = position.getX();
        int y = position.getY();
        return Stream.of(
                new PointImpl(x - 2, y - 1),
                new PointImpl(x - 2, y + 1),
                new PointImpl(x + 2, y - 1),
                new PointImpl(x + 2, y + 1),
                new PointImpl(x - 1, y + 2),
                new PointImpl(x + 1, y + 2),
                new PointImpl(x - 1, y - 2),
                new PointImpl(x + 1, y - 2)
        ).filter(destination -> isAvailable(board, destination, color))
                .map(destination -> Move.from(position).to(destination))
                .collect(Collectors.toList());
    }

    @Override
    public List<Move> getAvailableMoves() {
        return availableMoves(board, position, color);
    }
}
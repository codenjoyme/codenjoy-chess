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
import com.codenjoy.dojo.services.QDirection;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.Direction.*;

public class Rook extends Piece {
    public Rook(Color color, GameBoard board, Point position) {
        super(Type.ROOK, color, board, position);
    }

    /**
     * The method calculates all available moves of rook
     * in accordance with described circumstances,
     * including those where enemy's piece can be taken.
     *
     * @param board    a chess board
     * @param position a position of a bishop
     * @param color    a color of the bishop
     * @return all available moves
     */
    public static List<Move> availableMoves(GameBoard board, Point position, Color color) {
        return Stream.of(LEFT, UP, RIGHT, DOWN)
                .map(direction -> movesInQDirection(board, position, color, toQDirection(direction)))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static QDirection toQDirection(Direction direction) {
        switch (direction) {
            case LEFT:
                return QDirection.LEFT;
            case RIGHT:
                return QDirection.RIGHT;
            case UP:
                return QDirection.UP;
            case DOWN:
                return QDirection.DOWN;
            default:
                throw new IllegalArgumentException("Direction " + direction + " not supported");
        }
    }

    @Override
    public List<Move> getAvailableMoves() {
        return availableMoves(board, position, color);
    }
}

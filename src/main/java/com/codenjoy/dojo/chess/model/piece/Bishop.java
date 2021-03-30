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
import com.codenjoy.dojo.chess.model.Field;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;

public class Bishop extends Piece {

    public Bishop(PieceType type, Color color, Point position) {
        super(type, color, position);
    }

    @Override
    public List<Point> getAvailableMoves(Field field) {
        ArrayList<Point> moves = Lists.newArrayList();
        moves.addAll(availableDiagonalMoves(field, LEFT, UP));
        moves.addAll(availableDiagonalMoves(field, UP, RIGHT));
        moves.addAll(availableDiagonalMoves(field, RIGHT, DOWN));
        moves.addAll(availableDiagonalMoves(field, DOWN, LEFT));
        return moves;
    }

    private List<Point> availableDiagonalMoves(Field field, Direction one, Direction two) {
        List<Point> result = Lists.newArrayList();
        Point dest = diagonal(position, one, two);
        while (field.getAt(dest).isEmpty()) {
            result.add(dest);
            dest = diagonal(dest, one, two);
        }
        if (field.getAt(dest).get().getColor() != color) {
            result.add(dest);
        }
        return result;
    }


    private Point diagonal(Point position, Direction one, Direction two) {
        return one.change(two.change(position));
    }
}

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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.services.Direction.*;

public class Queen extends Piece {

    public Queen(Color color, Board board, Point position) {
        super(Type.QUEEN, color, board, position);
    }

    @Override
    public List<Move> getAvailableMoves() {
        List<Move> result = Lists.newArrayList();
        result.addAll(foo(UP));
        result.addAll(foo(DOWN));
        result.addAll(foo(LEFT));
        result.addAll(foo(RIGHT));
        result.addAll(availableDiagonalMoves(board, LEFT, UP));
        result.addAll(availableDiagonalMoves(board, UP, RIGHT));
        result.addAll(availableDiagonalMoves(board, RIGHT, DOWN));
        result.addAll(availableDiagonalMoves(board, DOWN, LEFT));
        return result;
    }

    private List<Move> foo(Direction d) {
        Point p = d.change(position);
        List<Point> points = Lists.newArrayList();
        while (board.isInBounds(p) && board.getAt(p).isEmpty()) {
            points.add(p);
            p = d.change(p);
        }
        if (board.isInBounds(p) && board.getAt(p).isPresent() && board.getAt(p).get().getColor() != color) {
            points.add(p);
        }
        return points.stream()
                .map(pt -> Move.from(position).to(pt))
                .collect(Collectors.toList());
    }

    private List<Move> availableDiagonalMoves(Board board, Direction one, Direction two) {
        List<Move> result = Lists.newArrayList();
        Point dest = diagonal(position, one, two);
        while (board.isInBounds(dest) && board.getAt(dest).isEmpty()) {
            result.add(Move.from(position).to(dest));
            dest = diagonal(dest, one, two);
        }
        if (board.isInBounds(dest) && board.getAt(dest).isPresent() && board.getAt(dest).get().getColor() != color) {
            result.add(Move.from(position).to(dest));
        }
        return result;
    }


    private Point diagonal(Point position, Direction one, Direction two) {
        return one.change(two.change(position));
    }
}

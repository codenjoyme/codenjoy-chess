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
import com.codenjoy.dojo.chess.model.Board;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class Rook extends Piece {
    public Rook(Color color, Board board, Point position) {
        super(PieceType.ROOK, color, board, position);
    }

    public void move(Point position) {
        board.getAt(position).ifPresent(p -> p.setAlive(false));
        this.position = position;
        moved = true;
    }

    @Override
    public void move(Move move) {
        super.move(move);
        moved = true;
    }

    @Override
    public List<Move> getAvailableMoves() {
        List<Move> result = Lists.newArrayList();
        result.addAll(foo(Direction.UP));
        result.addAll(foo(Direction.DOWN));
        result.addAll(foo(Direction.LEFT));
        result.addAll(foo(Direction.RIGHT));
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
}

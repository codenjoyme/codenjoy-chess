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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.List;

public class Pawn extends Piece {
    private boolean moved;

    public Pawn(Color color, Board board, Point position) {
        super(PieceType.PAWN, color, board, position);
    }

    @Override
    public void move(Point destination) {
        super.move(destination);
        moved = true;
    }

    @Override
    public List<Point> getAvailableMoves() {
        List<Point> moves = Lists.newArrayList();
        Point step = getAttackDirection().change(position);
        if (board.getAt(step).isEmpty()) {
            moves.add(step);
            if (!moved) {
                step = getAttackDirection().change(step);
                if (board.getAt(step).isEmpty()) {
                    moves.add(step);
                }
            }
        }
        step = getAttackDirection().change(position);
        if (board.getAt(Direction.RIGHT.change(step)).isPresent()) {
            moves.add(Direction.RIGHT.change(step));
        }
        if (board.getAt(Direction.LEFT.change(step)).isPresent()) {
            moves.add(Direction.LEFT.change(step));
        }
        return moves;
    }
}
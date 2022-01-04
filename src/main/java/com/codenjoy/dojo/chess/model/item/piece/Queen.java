package com.codenjoy.dojo.chess.model.item.piece;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.chess.model.HeroColor;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.model.GameBoard;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.List;

// https://en.wikipedia.org/wiki/Queen_(chess)
public class Queen extends Piece {

    public Queen(HeroColor color, GameBoard board, Point position) {
        super(Type.QUEEN, color, board, position);
    }

    /**
     * The method calculates all available moves of a queen
     * in accordance with described circumstances
     * including those where enemy's piece can be taken.
     *
     * @param board    a chess board
     * @param position a position of a queen
     * @param color    a color of the queen
     * @return all available moves
     */
    public static List<Move> availableMoves(GameBoard board, Point position, HeroColor color) {
        List<Move> result = Lists.newArrayList();
        result.addAll(Rook.availableMoves(board, position, color));
        result.addAll(Bishop.availableMoves(board, position, color));
        return result;
    }

    @Override
    public List<Move> getAvailableMoves() {
        return availableMoves(board, position, color);
    }
}

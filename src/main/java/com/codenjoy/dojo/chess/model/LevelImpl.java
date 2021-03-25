package com.codenjoy.dojo.chess.model;

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


import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.chess.model.piece.*;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static com.codenjoy.dojo.chess.model.Elements.*;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Piece> getFigures(boolean isWhite) {
        return LevelUtils.getObjects(xy, map,
                new HashMap<Elements, Function<Point, Piece>>(){{
                    put(WHITE_QUEEN, pt -> new Queen(pt, true));
                    put(WHITE_KNIGHT, pt -> new Knight(pt, true));
                    put(WHITE_KING, pt -> new King(pt, true));
                    put(WHITE_BISHOP, pt -> new Bishop(pt, true));
                    put(WHITE_PAWN, pt -> new Pawn(pt, true));
                    put(WHITE_ROOK, pt -> new Rook(pt, true));
                    put(BLACK_QUEEN, pt -> new Queen(pt, false));
                    put(BLACK_KNIGHT, pt -> new Knight(pt, false));
                    put(BLACK_KING, pt -> new King(pt, false));
                    put(BLACK_ROOK, pt -> new Rook(pt, false));
                    put(BLACK_PAWN, pt -> new Pawn(pt, false));
                    put(BLACK_BISHOP, pt -> new Bishop(pt, false));
                }});
    }

    private char upper(char ch) {
        return ("" + ch).toUpperCase().charAt(0);
    }
}

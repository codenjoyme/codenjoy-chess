package com.codenjoy.dojo.chess.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.chess.model.Elements;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementsMapper {
    private static final Map<Color, List<Elements>> colorsToPieces = new HashMap<>();

    static {
        colorsToPieces.put(Color.WHITE, Lists.newArrayList(
                Elements.WHITE_KING,
                Elements.WHITE_QUEEN,
                Elements.WHITE_BISHOP,
                Elements.WHITE_ROOK,
                Elements.WHITE_KNIGHT,
                Elements.WHITE_PAWN
        ));
        colorsToPieces.put(Color.BLACK, Lists.newArrayList(
                Elements.BLACK_KING,
                Elements.BLACK_QUEEN,
                Elements.BLACK_BISHOP,
                Elements.BLACK_ROOK,
                Elements.BLACK_KNIGHT,
                Elements.BLACK_PAWN
        ));
        colorsToPieces.put(Color.RED, Lists.newArrayList(
                Elements.RED_KING,
                Elements.RED_QUEEN,
                Elements.RED_BISHOP,
                Elements.RED_ROOK,
                Elements.RED_KNIGHT,
                Elements.RED_PAWN
        ));
        colorsToPieces.put(Color.BLUE, Lists.newArrayList(
                Elements.BLUE_KING,
                Elements.BLUE_QUEEN,
                Elements.BLUE_BISHOP,
                Elements.BLUE_ROOK,
                Elements.BLUE_KNIGHT,
                Elements.BLUE_PAWN
        ));
    }

    public static List<Elements> getElements(Color color) {
        return colorsToPieces.get(color);
    }

    public static Color getColor(Elements piece) {
        return colorsToPieces.entrySet().stream()
            .filter(entry -> entry.getValue().contains(piece))
            .map(Map.Entry::getKey)
            .findAny()
            .orElse(null);
    }
}

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
import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.utils.LevelUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.chess.model.Element.SQUARE;

public class LevelImpl implements Level {

    private final LengthToXY xy;

    private final String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Piece> pieces(Color color) {
        switch (color) {
            case WHITE:
                return LevelUtils.getObjects(xy, map, PiecesFactory::create, Element.whitePieces());
            case BLACK:
                return LevelUtils.getObjects(xy, map, PiecesFactory::create, Element.blackPieces());
            default:
                throw new IllegalArgumentException("Color " + color + " is not supported");
        }
    }

    @Override
    public List<Color> presentedColors() {
        Set<Color> presented = Sets.newHashSet();
        for (int i = 0; i < map.length(); i++) {
            Optional.ofNullable(Element.of(map.charAt(i)))
                    .map(Element::color)
                    .ifPresent(presented::add);
        }
        return Lists.newArrayList(presented);
    }

    @Override
    public List<Square> squares() {
        return LevelUtils.getObjects(xy, map, Function.identity(), Element.values())
                .stream()
                .map(p -> new Square(SQUARE, p))
                .collect(Collectors.toList());
    }
}

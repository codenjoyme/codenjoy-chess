package com.codenjoy.dojo.chess.model.level;

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
import com.codenjoy.dojo.chess.model.ElementMapper;
import com.codenjoy.dojo.chess.model.item.Barrier;
import com.codenjoy.dojo.chess.model.item.Square;
import com.codenjoy.dojo.games.chess.Element;
import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.AbstractLevel;
import com.codenjoy.dojo.utils.LevelUtils;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.games.chess.Element.BARRIER;
import static com.codenjoy.dojo.games.chess.Element.SQUARE;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    public List<Point> pieces(HeroColor color, Piece.Type type) {
        return find(Function.identity(), ElementMapper.mapToElement(color, type));
    }

    public List<HeroColor> presentedColors() {
        List<HeroColor> result = new LinkedList<>();
        for (int i = 0; i < map.length(); i++) {
            Optional.ofNullable(Element.of(map.charAt(i)))
                    .filter(e -> Lists.newArrayList(Element.pieces()).contains(e))
                    .map(ElementMapper::mapToColor)
                    .filter(e -> !result.contains(e))
                    .ifPresent(result::add);
        }
        return result;
    }

    public List<Square> squares() {
        return new LinkedList<>(){{
            addAll(find(Square::new, SQUARE));
            addAll(find(Square::new, Element.pieces()));
        }};
    }

    public List<Barrier> barriers() {
        return find(Barrier::new, BARRIER);
    }
}
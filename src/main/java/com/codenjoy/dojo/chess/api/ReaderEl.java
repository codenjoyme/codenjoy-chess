package com.codenjoy.dojo.chess.api;

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

import com.codenjoy.dojo.chess.model.Element;
import com.codenjoy.dojo.chess.model.ElementMapper;
import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class ReaderEl extends PointImpl implements State<Element, Player> {
    private final Element element;

    public static ReaderEl create(Piece piece) {
        Point position = piece.getPosition();
        Element element = ElementMapper.mapToElement(piece);
        return new ReaderEl(position, element);
    }

    public ReaderEl(Point position, Element element) {
        super(position);
        this.element = element;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return element;
    }
}

package com.codenjoy.dojo.chess.client;

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


import com.codenjoy.dojo.chess.model.Elements;
import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@see AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
@SuppressWarnings("unused")
public class Board extends AbstractBoard<Elements> {

    @Override
    protected int inversionY(int y) {
        return size() - 1 - y;
    }

    @Override
    public Elements valueOf(char ch) {
        return Elements.of(ch);
    }

    @Override
    public String toString() {
        return String.format("%s", boardAsString());
    }

    public Elements getAt(int x, int y) {
        if (isOutOfField(x, y)) {
            return null;
        }
        return super.getAt(x, y);
    }

    public List<Point> getBarriers() {
        return get(Elements.BARRIER);
    }

    public boolean isBarrier(int x, int y) {
        return getAt(x, y) == Elements.BARRIER;
    }

    public boolean isBarrier(Point position) {
        return isBarrier(position.getX(), position.getY());
    }

    public Color getColor(int x, int y) {
        return ElementsMapper.getColor(getAt(x, y));
    }

    public Color getColor(Point position) {
        return getColor(position.getX(), position.getY());
    }

    public List<Point> getSquares() {
        ArrayList<Point> positions = Lists.newArrayList();
        positions.addAll(get(Elements.SQUARE));
        positions.addAll(get(Elements.pieces()));
        return positions;
    }

    public List<Point> getPieces(Color color) {
        Elements[] elements = ElementsMapper.getElements(color).toArray(Elements[]::new);
        return get(elements);
    }
}

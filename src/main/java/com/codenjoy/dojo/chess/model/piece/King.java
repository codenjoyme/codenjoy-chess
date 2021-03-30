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
import com.codenjoy.dojo.chess.model.Field;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.services.Direction.*;

public class King extends Piece {

    public King(PieceType type, Color color, Point position) {
        super(type, color, position);
    }

    @Override
    public List<Point> getAvailableMoves(Field field) {
        List<Point> moves = listOfAvailableMoves(field,
                LEFT.change(position),
                UP.change(position),
                RIGHT.change(position),
                DOWN.change(position),
                LEFT.change(UP.change(position)),
                UP.change(RIGHT.change(position)),
                RIGHT.change(DOWN.change(position)),
                DOWN.change(LEFT.change(position))
        );
        return null;
    }

    private List<Point> listOfAvailableMoves(Field field, Point... destinations) {
        List<Point> result = Lists.newArrayList();
        for (Point dest : destinations) {
            if (isAvailable(field, dest)) {
                result.add(dest);
            }
        }
        return result;
    }

    private boolean isAvailable(Field field, Point dest) {
        Optional<Piece> pieceAtDest = field.getAt(dest);
        return pieceAtDest.isEmpty() || pieceAtDest.get().getColor() != color;
    }

}

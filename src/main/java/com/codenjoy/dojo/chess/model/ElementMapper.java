package com.codenjoy.dojo.chess.model;

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

import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.chess.model.piece.PieceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.codenjoy.dojo.chess.model.Color.*;
import static com.codenjoy.dojo.chess.model.Element.*;
import static com.codenjoy.dojo.chess.model.piece.PieceType.*;

public class ElementMapper {
    private static final Map<ColorAndType, Element> elementsByColorAndType = new HashMap<>() {{
        put(new ColorAndType(WHITE, KING), WHITE_KING);
        put(new ColorAndType(WHITE, QUEEN), WHITE_QUEEN);
        put(new ColorAndType(WHITE, KNIGHT), WHITE_KNIGHT);
        put(new ColorAndType(WHITE, BISHOP), WHITE_BISHOP);
        put(new ColorAndType(WHITE, ROOK), WHITE_ROOK);
        put(new ColorAndType(WHITE, PAWN), WHITE_PAWN);

        put(new ColorAndType(BLACK, KING), BLACK_KING);
        put(new ColorAndType(BLACK, QUEEN), BLACK_QUEEN);
        put(new ColorAndType(BLACK, KNIGHT), BLACK_KNIGHT);
        put(new ColorAndType(BLACK, BISHOP), BLACK_BISHOP);
        put(new ColorAndType(BLACK, ROOK), BLACK_ROOK);
        put(new ColorAndType(BLACK, PAWN), BLACK_PAWN);
    }};

    public static Element mapToElement(Piece piece) {
        return mapToElement(piece.getColor(), piece.getType());
    }

    public static Element mapToElement(Color color, PieceType pieceType) {
        return elementsByColorAndType.get(new ColorAndType(color, pieceType));
    }

    private static class ColorAndType {
        private final Color color;
        private final PieceType type;

        public ColorAndType(Color color, PieceType type) {
            this.color = color;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ColorAndType that = (ColorAndType) o;
            return color == that.color && type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(color, type);
        }
    }
}

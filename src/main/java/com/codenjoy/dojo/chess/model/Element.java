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


import com.codenjoy.dojo.services.printer.CharElements;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public enum Element implements CharElements {

    SQUARE('.'),

    WHITE_KING('W'),
    WHITE_QUEEN('Q'),
    WHITE_ROOK('R'),
    WHITE_BISHOP('B'),
    WHITE_KNIGHT('K'),
    WHITE_PAWN('P'),

    BLACK_KING('w'),
    BLACK_QUEEN('q'),
    BLACK_ROOK('r'),
    BLACK_BISHOP('b'),
    BLACK_KNIGHT('k'),
    BLACK_PAWN('p');

    final char ch;

    Element(char ch) {
        this.ch = ch;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }


    public Color color() {
        if (Arrays.asList(whitePieces()).contains(this)) {
            return Color.WHITE;
        }
        if (Arrays.asList(blackPieces()).contains(this)) {
            return Color.BLACK;
        }
        return null;
    }

    public static Element of(char ch) {
        for (Element element : Element.values()) {
            if (element.ch == ch) {
                return element;
            }
        }
        return null;
    }

    public static Element[] getPieces() {
        return ArrayUtils.addAll(whitePieces(), blackPieces());
    }

    public static Element[] piecesOfColor(Color color) {
        switch (color) {
            case WHITE:
                return whitePieces();
            case BLACK:
                return blackPieces();
            default:
                throw new IllegalArgumentException("Color " + color + " is not supported");
        }
    }

    public static Element[] whitePieces() {
        return new Element[]{WHITE_KING, WHITE_QUEEN, WHITE_ROOK, WHITE_BISHOP, WHITE_KNIGHT, WHITE_PAWN};
    }

    public static Element[] blackPieces() {
        return new Element[]{BLACK_KING, BLACK_QUEEN, BLACK_ROOK, BLACK_BISHOP, BLACK_KNIGHT, BLACK_PAWN};
    }
}

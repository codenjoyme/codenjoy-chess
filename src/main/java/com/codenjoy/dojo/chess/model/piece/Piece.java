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
import com.codenjoy.dojo.chess.model.Element;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.model.ReaderEl;
import com.codenjoy.dojo.services.Point;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;

public abstract class Piece {

    protected final Color color;
    protected final Element element;
    protected Point position;
    protected boolean alive;

    public Piece(Element element, Point position) {
        this.position = position;
        this.element = element;
        this.alive = true;
        if (Arrays.asList(Element.whitePieces()).contains(element)) {
            this.color = WHITE;
        } else if (Arrays.asList(Element.blackPieces()).contains(element)) {
            this.color = BLACK;
        } else {
            throw new IllegalArgumentException("Color of element " + element + " is not supported");
        }
    }

    public void move(Point destination) {
//        if (!getMoves().contains(destination)) {
            // ?
//            return;
//        }
        position = destination;
    }

    public ReaderEl toReaderEl() {
        return new ReaderEl(position, element);
    }

    public Color getColor() {
        return color;
    }

    public Point getPosition() {
        return position;
    }

    public boolean isAlive() {
        return alive;
    }

    public Element getElement() {
        return element;
    }

    public static Piece create(Point position, Element element) {
        switch (element) {
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                return new Bishop(element, position);

            case WHITE_KING:
            case BLACK_KING:
                return new King(element, position);

            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                return new Knight(element, position);

            case WHITE_PAWN:
            case BLACK_PAWN:
                return new Pawn(element, position);

            case WHITE_QUEEN:
            case BLACK_QUEEN:
                return new Queen(element, position);

            case WHITE_ROOK:
            case BLACK_ROOK:
                return new Rook(element, position);

            default:
                throw new IllegalArgumentException("Element " + element + " is not a chess piece");
        }
    }

    public abstract List<Point> getMoves();
}

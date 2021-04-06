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
import com.codenjoy.dojo.chess.model.Board;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.List;

public abstract class Piece {

    protected final Color color;
    protected final PieceType type;
    protected final Board board;
    protected Point position;
    protected boolean alive;

    public Piece(PieceType type, Color color, Board board, Point position) {
        this.type = type;
        this.color = color;
        this.board = board;
        this.position = position;
        this.alive = true;
    }

    public static Piece create(PieceType type, Color color, Board board, Point position) {
        return type.getConstructor().apply(color, board, position);
    }

    public void move(Point destination) {
        if (!getAvailableMoves().contains(destination)) {
            return;
        }
        board.getAt(destination).ifPresent(p -> p.setAlive(false));
        position = destination;
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

    public PieceType getType() {
        return type;
    }

    public abstract List<Point> getAvailableMoves();

    public Direction getAttackDirection() {
        return color == Color.WHITE ? Direction.UP : Direction.DOWN;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}

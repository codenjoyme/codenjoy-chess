package com.codenjoy.dojo.chess.model.item.piece;

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
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.service.GameBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.google.common.collect.Lists;

import java.util.List;

public abstract class Piece {

    protected final Color color;
    protected final Type type;
    protected final GameBoard board;
    protected Point position;
    protected boolean alive;
    protected boolean moved;
    protected List<Move> committedMoves;
    protected Direction attackDirection;

    public Piece(Type type, Color color, GameBoard board, Point position) {
        this.type = type;
        this.color = color;
        this.board = board;
        this.position = position;
        this.alive = true;
        this.committedMoves = Lists.newArrayList();
        this.attackDirection = color.getAttackDirection();
    }

    public static Piece create(Type type, Color color, GameBoard board, Point position) {
        switch (type) {
            case KING:
                return new King(color, board, position);
            case QUEEN:
                return new Queen(color, board, position);
            case KNIGHT:
                return new Knight(color, board, position);
            case ROOK:
                return new Rook(color, board, position);
            case BISHOP:
                return new Bishop(color, board, position);
            case PAWN:
                return new Pawn(color, board, position);
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }

    protected static Point diagonal(Point position, Direction one, Direction two) {
        if (one == two || one.inverted() == two) {
            throw new IllegalArgumentException("Directions should be perpendicular for diagonal position calculation");
        }
        return one.change(two.change(position));
    }

    /**
     * The method calculates all available moves in specific direction
     * for pieces, which moves diagonally, vertically or horizontally,
     * can not move throughout another pieces
     * and can take enemy's piece.
     *
     * @param board     a chess board
     * @param position  a position of a bishop
     * @param color     a color of the bishop
     * @param direction a direction of attack of the bishop
     * @return all available moves in specific direction
     */
    protected static List<Move> movesInQDirection(GameBoard board, Point position, Color color, QDirection direction) {
        List<Move> moves = Lists.newArrayList();
        Point destination = direction.change(position);
        while (board.isInBounds(destination) && board.getPieceAt(destination).isEmpty()) {
            moves.add(Move.from(position).to(destination));
            destination = direction.change(destination);
        }
        // checks attack move
        if (board.getPieceAt(destination).isPresent())
            if (board.getPieceAt(destination).get().getColor() != color) {
                moves.add(Move.from(position).to(destination));
            }
        return moves;
    }

    public boolean isMoved() {
        return moved;
    }

    public boolean move(Point position) {
        board.getPieceAt(position).ifPresent(p -> p.setAlive(false));
        committedMoves.add(Move.from(this.position).to(position));
        moved = true;
        this.position = position;
        return true;
    }

    public boolean move(Move move) {
        if (!move.getFrom().equals(position)) {
            return false;
        }
        return move(move.getTo());
    }

    public Color getColor() {
        return color;
    }

    public Point getPosition() {
        return position.copy();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Type getType() {
        return type;
    }

    public abstract List<Move> getAvailableMoves();

    public boolean isAttacks(Point position) {
        return getAvailableMoves().stream()
                .map(Move::getTo)
                .anyMatch(p -> p.equals(position));
    }

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", type=" + type +
                ", position=" + position +
                ", alive=" + alive +
                ", moved=" + moved +
                '}';
    }

    public enum Type {
        KING(0),
        QUEEN(1),
        KNIGHT(2),
        BISHOP(3),
        ROOK(4),
        PAWN(5);

        private final int id;

        Type(int id) {
            this.id = id;
        }

        public static Type byId(int id) {
            for (Type type : Type.values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }

        public int getId() {
            return id;
        }
    }
}

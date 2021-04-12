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

import com.codenjoy.dojo.chess.model.piece.*;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameSet extends PlayerHero<Board> {
    private final static Logger LOGGER = LoggerFactory.getLogger(GameSet.class);

    private final List<Piece> pieces;
    private boolean triedWrongMove;
    private Move lastMove;
    private Move command;

    public GameSet(List<Piece> pieces, Board board) {
        if (pieces.isEmpty()) {
            throw new IllegalArgumentException("Game set should contain at least one piece");
        }
        List<Piece> kings = pieces.stream().filter(p -> p.getType() == Piece.Type.KING)
                .collect(Collectors.toList());
        if (kings.size() != 1) {
            throw new IllegalArgumentException("Should be exactly 1 king piece in game set");
        }
        this.pieces = pieces;
        init(board);
    }

    public boolean isAlive() {
        return pieces.stream()
                .filter(p -> p.getType() == Piece.Type.KING)
                .findAny()
                .map(Piece::isAlive)
                .orElse(false);
    }

    public Color getColor() {
        return pieces.get(0).getColor();
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public Optional<Piece> getPieceAt(int x, int y) {
        return getPieceAt(new PointImpl(x, y));
    }

    public Optional<Piece> getPieceAt(Point position) {
        return pieces.stream()
                .filter(p -> p.getPosition().equals(position))
                .findFirst();
    }

    public Map<Piece, List<Move>> getAvailableMoves() {
        return pieces.stream()
                .collect(Collectors.toMap(Function.identity(), Piece::getAvailableMoves));
    }

    @Override
    public void down() {
        // not supported
    }

    @Override
    public void up() {
        // not supported
    }

    @Override
    public void left() {
        // not supported
    }

    @Override
    public void right() {
        // not supported
    }

    @Override
    public void act(int... codes) {
        LOGGER.info("ACT{}, Color: {}", Arrays.toString(codes), getColor());
        if (field.getCurrentColor() == getColor()) {
            command = Move.decode(codes);
            if (command == null) {
                LOGGER.warn("Game set with color {} received invalid action parameters: {}", getColor(), Arrays.toString(codes));
            }
        }
    }

    @Override
    public void tick() {
        triedWrongMove = false;
        lastMove = null;
        if (command == null) {
            return;
        }
        // TODO: Piece can be null then wrong move
        Piece piece = field.getAt(command.getFrom())
                .orElse(null);
        if (piece == null || piece.getColor() != getColor()) {
            triedWrongMove = true;
            lastMove = null;
            command = null;
            return;
        }
        if (piece.getAvailableMoves().contains(command)) {
            if (isCastling(command)) {
                // castling
                if (!tryCastling((Rook) field.getAt(command.getTo()).get())) {
                    triedWrongMove = true;
                    lastMove = null;
                } else {
                    lastMove = command;
                }
                command = null;
                return;
            } else {
                piece.move(command);
                lastMove = command;
                if (command.withPromotion()) {
                    pieces.remove(piece);
                    pieces.add(command.getPromotion().getConstructor().apply(getColor(), field, piece.getPosition()));
                }
            }
        } else {
            triedWrongMove = true;
            lastMove = null;
        }
        command = null;
    }

    private boolean isCastling(Move command) {
        Optional<Piece> optPieceOne = field.getAt(command.getFrom());
        Optional<Piece> optPieceTwo = field.getAt(command.getTo());
        if (optPieceOne.isEmpty() || optPieceTwo.isEmpty()) {
            return false;
        }
        Piece pieceOne = optPieceOne.get();
        Piece pieceTwo = optPieceTwo.get();
        if (pieceOne.getColor() != pieceTwo.getColor()) {
            return false;
        }
        return pieceOne.getType() == Piece.Type.KING && pieceTwo.getType() == Piece.Type.ROOK;
    }

    private boolean tryCastling(Rook rook) {
        King king = getKing();
        if (king == null) {
            throw new IllegalStateException("King is null");
        }
        if (rook.isMoved() || king.isMoved()) {
            return false;
        }
        if (field.isUnderAttack(king.getPosition(), getColor())) {
            return false;
        }
        Direction direction = defineDirection(king.getPosition(), rook.getPosition());
        if (direction == null) {
            throw new IllegalStateException();
        }
        Point rookPosition = direction.change(king.getPosition());
        if (field.getAt(rookPosition).isPresent() || field.isUnderAttack(rookPosition, getColor())) {
            return false;
        }
        Point kingPosition = direction.change(rookPosition);
        if (field.getAt(kingPosition).isPresent() || field.isUnderAttack(kingPosition, getColor())) {
            return false;
        }
        rook.move(rookPosition);
        king.move(kingPosition);
        return true;
    }

    private Direction defineDirection(Point from, Point to) {
        if (from.equals(to)) {
            return null;
        }
        if (from.getX() == to.getX()) {
            return from.getY() < to.getY() ? Direction.UP : Direction.DOWN;
        }
        if (from.getY() == to.getY()) {
            return from.getX() < to.getX() ? Direction.RIGHT : Direction.LEFT;
        }
        return null;
    }

    private King getKing() {
        return (King) pieces.stream()
                .filter(p -> p.getType() == Piece.Type.KING)
                .findFirst().orElse(null);
    }

    public Move getLastMove() {
        return lastMove;
    }

    public boolean isTriedWrongMove() {
        return triedWrongMove;
    }
}

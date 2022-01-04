package com.codenjoy.dojo.chess.model;

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

import com.codenjoy.dojo.chess.model.item.piece.King;
import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.chess.model.item.piece.Rook;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameSet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSet.class);

    private final HeroColor color;
    private final List<Piece> pieces;
    private final GameBoard board;

    private Move lastMove;

    public GameSet(HeroColor color, GameBoard board, List<Piece> pieces) {
        this.color = color;
        this.board = board;
        if (pieces.stream().anyMatch(p -> p.getColor() != color)) {
            throw new IllegalArgumentException("All pieces should be " + color);
        }
        this.pieces = pieces;
    }

    public boolean isKingAlive() {
        return pieces.stream()
                .filter(p -> p.getType() == Piece.Type.KING)
                .anyMatch(Piece::isAlive);
    }

    public HeroColor getColor() {
        return color;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public boolean makeMove(final Move command) {
        if (command == null) {
            return false;
        }
        Piece piece = board.getPieceAt(command.getFrom())
                .orElse(null);
        if (piece == null || piece.getColor() != getColor()) {
            return false;
        }
        if (piece.getAvailableMoves().contains(command)) {
            if (isCastling(command)) {
                if (tryCastling((Rook) board.getPieceAt(command.getTo()).get())) {
                    lastMove = command;
                    return true;
                } else {
                    return false;
                }
            } else {
                piece.move(command);
                if (command.withPromotion()) {
                    pieces.remove(piece);
                    pieces.add(Piece.create(command.getPromotion(), getColor(), board, piece.getPosition()));
                }
                lastMove = command;
                return true;
            }
        }
        return false;
    }

    public void die() {
        pieces.forEach(p -> p.setAlive(false));
    }

    public List<Move> getAvailableMoves() {
        return pieces.stream()
                .map(Piece::getAvailableMoves)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Move getLastMove() {
        return lastMove;
    }

    private boolean isCastling(Move command) {
        Optional<Piece> optPieceOne = board.getPieceAt(command.getFrom());
        Optional<Piece> optPieceTwo = board.getPieceAt(command.getTo());
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
        if (board.isUnderAttack(king.getPosition(), getColor())) {
            return false;
        }
        Direction direction = defineDirection(king.getPosition(), rook.getPosition());
        if (direction == null) {
            throw new IllegalStateException();
        }
        Point rookPosition = direction.change(king.getPosition());
        if (board.getPieceAt(rookPosition).isPresent() || board.isUnderAttack(rookPosition, getColor())) {
            return false;
        }
        Point kingPosition = direction.change(rookPosition);
        if (board.getPieceAt(kingPosition).isPresent() || board.isUnderAttack(kingPosition, getColor())) {
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
}

package com.codenjoy.dojo.chess.engine.service;

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

import com.codenjoy.dojo.chess.engine.level.Level;
import com.codenjoy.dojo.chess.engine.model.Color;
import com.codenjoy.dojo.chess.engine.model.item.Barrier;
import com.codenjoy.dojo.chess.engine.model.item.Square;
import com.codenjoy.dojo.chess.engine.model.item.piece.King;
import com.codenjoy.dojo.chess.engine.model.item.piece.Piece;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameBoard {
    private final Level level;
    private final List<Square> squares;
    private final List<Barrier> barriers;
    private final List<GameSet> gameSets;

    public GameBoard(String map) {
        this(new Level(map.replaceAll("[\\n\\t ]", "")));
    }

    public GameBoard(Level level) {
        this.level = level;
        this.squares = level.squares();
        this.barriers = level.barriers();
        this.gameSets = new ArrayList<>();

        for (Color color : level.presentedColors()) {
            List<Piece> pieces = Lists.newArrayList();
            for (Piece.Type pieceType : Piece.Type.values()) {
                pieces.addAll(level.pieces(color, pieceType).stream()
                        .map(position -> Piece.create(pieceType, color, this, position))
                        .collect(Collectors.toList()));
            }
            gameSets.add(new GameSet(color, this, pieces));
        }
    }

    public boolean tryMove(Color color, Move move) {
        return getGameSet(color).makeMove(move);
    }

    private List<GameSet> getAliveSets() {
        return gameSets.stream()
                .filter(GameSet::isKingAlive)
                .collect(Collectors.toList());
    }

    public Optional<Piece> getPieceAt(Point position) {
        return gameSets.stream()
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .filter(Piece::isAlive)
                .filter(p -> p.getPosition().equals(position))
                .findAny();
    }

    public boolean isUnderAttack(Point point, Color color) {
        // TODO refactor
        boolean result = gameSets.stream()
                .filter(gameSet -> gameSet.getColor() != color)
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .filter(p -> p.getType() != Piece.Type.KING)
                .map(Piece::getAvailableMoves)
                .flatMap(Collection::stream)
                .anyMatch(m -> m.getTo().equals(point));
        boolean kingAttack = gameSets.stream()
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .filter(p -> p.getType() == Piece.Type.KING && p.getColor() != color)
                .anyMatch(king -> King.availableMoves(this, point, color, king.isMoved(), false, Color.WHITE.getAttackDirection()).stream()
                        .map(Move::getTo)
                        .anyMatch(p -> p.equals(point)));
        boolean pawnAttack = gameSets.stream()
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .filter(p -> p.getType() == Piece.Type.PAWN && p.getColor() != color)
                .anyMatch(pawn -> {
                    Direction direction = pawn.getAttackDirection();
                    Direction attack1 = direction.clockwise();
                    Direction attack2 = direction.counterClockwise();
                    return attack1.change(direction.change(pawn.getPosition())).equals(point) || attack2.change(direction.change(pawn.getPosition())).equals(point);
                });
        return result || pawnAttack || kingAttack;
    }

    public int getSize() {
        return level.getSize();
    }

    public List<Color> getColors() {
        return gameSets.stream()
                .map(GameSet::getColor)
                .collect(Collectors.toList());
    }

    public boolean isInBounds(Point point) {
        return !point.isOutOf(getSize());
    }

    public List<Square> getSquares() {
        return squares;
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public List<Piece> getPieces() {
        return gameSets.stream()
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void die(Color color) {
        gameSets.stream()
                .filter(gameSet -> gameSet.getColor() == color)
                .findAny()
                .ifPresent(GameSet::die);
    }


    public boolean isAlive(Color color) {
        GameSet gameSet = getGameSet(color);
        if (gameSet == null) {
            throw new IllegalArgumentException("Game set with color " + color + " not exists");
        }
        return gameSet.isKingAlive();
    }

    private GameSet getGameSet(Color color) {
        return gameSets.stream()
                .filter(gameSet -> gameSet.getColor() == color)
                .findAny()
                .orElse(null);
    }

    public boolean isWinner(Color color) {
        List<GameSet> aliveSets = getAliveSets();
        return aliveSets.size() == 1 && aliveSets.get(0).getColor() == color;
    }

    public Move getLastMoveOf(Color color) {
        return getGameSet(color).getLastMove();
    }
}

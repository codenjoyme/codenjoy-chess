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

import com.codenjoy.dojo.chess.model.item.Barrier;
import com.codenjoy.dojo.chess.model.item.Square;
import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class GameBoard {

    private final Level level;
    private final List<Square> squares;
    private final List<Barrier> barriers;
    private final List<GameSet> gameSets;

    public GameBoard(String map) {
        this(new Level(map));
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
                        .collect(toList()));
            }
            gameSets.add(new GameSet(color, this, pieces));
        }
    }

    public boolean isAlive(Color color) {
        GameSet gameSet = getGameSet(color);
        if (gameSet == null) {
            throw new IllegalArgumentException("Game set with color " + color + " not exists");
        }
        return gameSet.isKingAlive();
    }

    public boolean tryMove(Color color, Move move) {
        return getGameSet(color).makeMove(move);
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
        return gameSets.stream()
                .filter(gameSet -> gameSet.getColor() != color)
                .filter(GameSet::isKingAlive)
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .anyMatch(p -> p.isAttacks(point));
    }

    public boolean isInBounds(Point position) {
        boolean outOfBounds = position.isOutOf(getSize());
        outOfBounds |= barriers.stream().anyMatch(b -> b.getPosition().equals(position));
        return !outOfBounds;
    }

    public void die(Color color) {
        gameSets.stream()
                .filter(gameSet -> gameSet.getColor() == color)
                .findAny()
                .ifPresent(GameSet::die);
    }

    public int getSize() {
        return level.getSize();
    }

    public List<Color> getColors() {
        return gameSets.stream()
                .map(GameSet::getColor)
                .collect(toList());
    }

    public List<Piece> getPieces() {
        return gameSets.stream()
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public List<Square> getSquares() {
        return squares;
    }

    public boolean isWinner(Color color) {
        List<Piece> enemyPieces = getEnemyPieces(color);
        List<GameSet> aliveSets = getAliveSets();
        return aliveSets.size() == 1
                && aliveSets.get(0).getColor() == color
                && enemyPieces.size() == 0;
    }

    private List<Piece> getEnemyPieces(Color color) {
        return getAlivePieces().stream()
                .filter(p -> p.getColor() != color)
                .collect(toList());
    }

    public List<Move> getAvailableMoves(Color color) {
        GameSet gameSet = getGameSet(color);
        if (!gameSet.isKingAlive()) {
            return Lists.newArrayList();
        }
        return gameSet.getAvailableMoves();
    }

    public Move getLastMoveOf(Color color) {
        GameSet gameSet = getGameSet(color);
        if (gameSet == null) {
            return null;
        }
        return gameSet.getLastMove();
    }

    public List<Piece> getAlivePieces() {
        return getPieces().stream()
                .filter(Piece::isAlive)
                .collect(toList());
    }

    private List<GameSet> getAliveSets() {
        return gameSets.stream()
                .filter(GameSet::isKingAlive)
                .collect(toList());
    }

    private GameSet getGameSet(Color color) {
        return gameSets.stream()
                .filter(gameSet -> gameSet.getColor() == color)
                .findAny()
                .orElse(null);
    }
}

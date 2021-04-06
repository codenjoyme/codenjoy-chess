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


import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.chess.model.piece.PieceType;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

public class Chess implements Board {

    private final Dice dice;

    private final GameSettings settings;
    private final Level level;

    private final List<Player> players = Lists.newLinkedList();
    private final List<Move> history = Lists.newArrayList();

    private int currentPlayerId;

    public Chess(Level level, Dice dice, GameSettings settings) {
        this.dice = dice;
        this.settings = settings;
        this.level = level;
    }

    @Override
    public void tick() {
        Player player = players.get(currentPlayerId);
        Move move = player.makeMove();
        if (move != null) {
            history.add(move);
        }
        currentPlayerId = (currentPlayerId + 1) % players.size();
    }

    public int size() {
        return level.getSize();
    }

    public List<Piece> getPieces() {
        return players.stream()
                .map(Player::getGameSet)
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Piece> getAt(Point position) {
        return getPieces().stream()
                .filter(p -> p.getPosition().equals(position))
                .findAny();
    }

    @Override
    public List<Move> getPossibleMoves(Piece piece) {
        List<Piece> pieces = getPieces();
        return null;
    }

    @Override
    public List<Move> getHistory() {
        return history;
    }

    @Override
    public Move getLastMove() {
        return history.isEmpty() ? null : history.get(history.size() - 1);
    }

    @Override
    public GameSet newGameSet() {
        Color color = players.stream()
                .map(Player::getGameSet)
                .filter(Objects::nonNull)
                .map(GameSet::getColor)
                .max(Comparator.comparingInt(Color::getPriority))
                .map(c -> Color.byPriority(c.getPriority() + 1))
                .orElse(Color.withHighestPriority());

        if (!level.presentedColors().contains(color)) {
            // log
            return null;
        }
        List<Piece> result = Lists.newArrayList();
        for (PieceType type : PieceType.values()) {
            List<Piece> pieces = level.pieces(color, type).stream()
                    .map(position -> Piece.create(type, color, this, position))
                    .collect(Collectors.toList());
            result.addAll(pieces);
        }
        return new GameSet(result, this);
    }

    @Override
    public int getSize() {
        return level.getSize();
    }

    @Override
    public void newGame(Player player) {
//        if (players.contains(player)) {
//         ???   
//        }
        if (players.size() == level.presentedColors().size()) {
            // players limit reached
            return;
        }

        players.add(player);
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {

            @Override
            public int size() {
                return Chess.this.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                ArrayList<ReaderEl> result = Lists.newArrayList();

                List<ReaderEl> squares = level.squares().stream()
                        .map(Square::toReaderEl)
                        .collect(Collectors.toList());

                List<ReaderEl> pieces = players.stream()
                        .map(Player::getGameSet)
                        .filter(Objects::nonNull)
                        .map(GameSet::getPieces)
                        .flatMap(Collection::stream)
                        .filter(Piece::isAlive)
                        .map(ReaderEl::create)
                        .collect(Collectors.toList());

                result.addAll(pieces);
                result.addAll(squares);
                return result;
            }
        };
    }

    @Override
    public GameSettings settings() {
        return settings;
    }
}

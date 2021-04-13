package com.codenjoy.dojo.chess.api;

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


import com.codenjoy.dojo.chess.model.*;
import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chess implements GameField<Player> {

    private final Dice dice;
    private final GameSettings settings;
    private final List<Player> players = Lists.newLinkedList();
    private final GameBoard board;

    public Chess(Level level, Dice dice, GameSettings settings) {
        this.board = new GameBoard(level);
        this.dice = dice;
        this.settings = settings;
    }

    @Override
    public void tick() {
        Player player = getPlayer(board.getCurrentColor());
        Move move = player.makeMove();
        if (move == null) {
            return;
        }
        players.stream()
                .filter(p -> !p.isAlive())
                .forEach(p -> p.event(Event.GAME_OVER));
        List<Player> alivePlayers = players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        if (alivePlayers.size() == 1) {
            alivePlayers.get(0).event(Event.WIN);
        }
    }

    @Override
    public void newGame(Player player) {
        if (board.getAvailableColors().isEmpty()) {
            // players limit exceeded
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
    public GameSettings settings() {
        return settings;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {

            @Override
            public int size() {
                return Chess.this.board.getSize();
            }

            @Override
            public Iterable<? extends Point> elements() {
                ArrayList<ReaderEl> result = Lists.newArrayList();

                List<ReaderEl> squares = board.getSquares().stream()
                        .map(Square::toReaderEl)
                        .collect(Collectors.toList());

                List<ReaderEl> barriers = board.getBarriers().stream()
                        .map(Barrier::toReaderEl)
                        .collect(Collectors.toList());

                List<ReaderEl> pieces = board.getPieces().stream()
                        .filter(Piece::isAlive)
                        .map(ReaderEl::create)
                        .collect(Collectors.toList());
                result.addAll(barriers);
                result.addAll(pieces);
                result.addAll(squares);
                return result;
            }
        };
    }

    public Color getCurrentColor() {
        return board.getCurrentColor();
    }

    public List<Color> getColors() {
        return board.getColors();
    }

    public Color getAvailableColor() {
       return board.getAvailableColors().get(0);
    }

    private Player getPlayer(Color color) {
        return players.stream()
                .filter(p -> p.getColor() == color)
                .findAny()
                .orElse(null);
    }

    public GameBoard getBoard() {
        return board;
    }
}
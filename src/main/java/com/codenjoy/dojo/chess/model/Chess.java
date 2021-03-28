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
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Chess implements Board {

    private final GameSettings settings;
    private final int size;
    private final Level level;
    private final List<Color> presentedColors;
    private final List<Player> players = new LinkedList<>();

    private final Dice dice;

    public Chess(Level level, Dice dice, GameSettings settings) {
        this.dice = dice;
        this.size = level.getSize();
        this.settings = settings;
        this.level = level;
        this.presentedColors = level.presentedColors();
    }

    @Override
    public void tick() {
        players.forEach(Player::tick);
    }

    public int size() {
        return size;
    }

    @Override
    public void newGame(Player player) {
//        if (players.contains(player)) {
//         ???   
//        }
        if (players.size() == presentedColors.size()) {
            // players limit reached
            return;
        }
        List<Color> clrs = players.stream()
                .map(Player::getGameSet)
                .map(GameSet::getColor)
                .collect(Collectors.toList());
        Color color = presentedColors.stream()
                .filter(c -> !clrs.contains(c))
                .findFirst()
                .orElse(null);

        if (color == null) {
            // log
            return;
        }

        players.add(player);
        player.initPieces(color, level.pieces(color));
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
                return Chess.this.size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                ArrayList<ReaderEl> result = Lists.newArrayList();

                List<ReaderEl> squares = level.squares().stream()
                        .map(Square::toReaderEl)
                        .collect(Collectors.toList());

                List<ReaderEl> pieces = players.stream()
                        .map(Player::getGameSet)
                        .map(GameSet::getPieces)
                        .flatMap(Collection::stream)
                        .map(Piece::toReaderEl)
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

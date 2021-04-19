package com.codenjoy.dojo.chess.service;

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
import com.codenjoy.dojo.chess.model.Events;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.chess.service.GameSettings.Option.WAIT_UNTIL_MAKE_A_MOVE;

public class Chess implements GameField<Player> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Chess.class);

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Dice dice;

    private final List<Player> players = Lists.newLinkedList();
    private final GameHistory history = new GameHistory();
    private final GameSettings settings;
    private final Rotator rotator;
    private final GameBoard board;

    private boolean playerAskedColor;
    private Color currentColor;

    public Chess(Level level, Dice dice, GameSettings settings) {
        this.dice = dice;
        this.settings = settings;
        this.board = new GameBoard(level);
        this.rotator = new Rotator(board.getSize());
        this.currentColor = getColors().stream()
                .min(Comparator.comparingInt(Color::getPriority))
                .orElseThrow(() -> new IllegalArgumentException("Level " + level + " is invalid"));
    }

    @Override
    public void tick() {
        List<Color> aliveBeforeTick = getAlivePlayers().stream()
                .map(Player::getColor)
                .collect(Collectors.toList());
        Player player = getPlayer(currentColor);

        playerAskedColor = player.askedForColor();
        if (playerAskedColor) {
            player.answeredColor();
            return;
        }

        Move move = player.makeMove();
        if (move == null && settings.bool(WAIT_UNTIL_MAKE_A_MOVE)) {
            LOGGER.debug(
                    "{} player's move didn't committed; " +
                            "Option {} set to true, so right to move is not transferred further",
                    currentColor, WAIT_UNTIL_MAKE_A_MOVE
            );
            return;
        }

        history.add(currentColor, move);
        currentColor = nextColor();
        checkGameOvers(aliveBeforeTick);
        checkVictory();
    }

    @Override
    public void newGame(Player player) {
        if (players.size() == getColors().size()) {
            LOGGER.warn("Trying to add new player [{}], but the game is already full", player);
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
    public BoardReader<Player> reader() {
        return new ChessBoardReader(this);
    }

    public int getBoardSize() {
        return board.getSize();
    }

    public Rotator getRotator() {
        return rotator;
    }

    public boolean isCurrentPlayerAskedColor() {
        return playerAskedColor;
    }

    public List<Player> getAlivePlayers() {
        return players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public List<Color> getColors() {
        return board.getColors();
    }

    public Color getAvailableColor() {
        Collection<Color> unusedColors = CollectionUtils.subtract(board.getColors(), getUsedColors());
        return unusedColors.size() > 0 ? unusedColors.iterator().next() : null;
    }

    public GameBoard getBoard() {
        return board;
    }

    private void checkGameOvers(List<Color> aliveBeforeTick) {
        List<Color> alive = getAlivePlayers().stream()
                .map(Player::getColor)
                .collect(Collectors.toList());
        Collection<Color> died = CollectionUtils.subtract(aliveBeforeTick, alive);
        died.forEach(color -> getPlayer(color).event(Events.GAME_OVER));
    }

    private void checkVictory() {
        List<Player> alivePlayers = getAlivePlayers();
        if (alivePlayers.size() == 1) {
            alivePlayers.get(0).event(Events.WIN);
        }
    }

    private List<Color> getUsedColors() {
        return players.stream()
                .map(Player::getColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Player getPlayer(Color color) {
        return players.stream()
                .filter(p -> p.getColor() == color)
                .findAny()
                .orElse(null);
    }

    private Color nextColor() {
        List<Player> alivePlayers = getAlivePlayers();
        alivePlayers.sort(Comparator.comparingInt(p -> p.getColor().getPriority()));
        return alivePlayers.stream()
                .map(Player::getColor)
                .filter(color -> color.getPriority() > currentColor.getPriority())
                .findAny()
                .orElse(alivePlayers.get(0).getColor());
    }
}
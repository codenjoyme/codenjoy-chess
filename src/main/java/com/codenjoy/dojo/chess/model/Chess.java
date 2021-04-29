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
import com.codenjoy.dojo.chess.service.*;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.chess.service.GameSettings.Option.WAIT_UNTIL_MAKE_A_MOVE;

public class Chess implements Field {
    private static final Logger LOGGER = LoggerFactory.getLogger(Chess.class);
    private static final Direction DEFAULT_ATTACK_DIRECTION = Color.WHITE.getAttackDirection();

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Dice dice;

    private final List<Player> players = Lists.newLinkedList();
    private final GameHistory history = new GameHistory();
    private final Set<Color> askedColors = new HashSet<>();
    private final GameSettings settings;
    private final Rotator rotator;
    private final GameBoard board;

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

    public boolean isPlayerAskedForColor(Player player) {
        return askedColors.contains(player.getColor());
    }

    public void colorAnswered(Color color) {
        askedColors.remove(color);
    }

    @Override
    public void tick() {
        List<Color> aliveBeforeTick = getAlivePlayers().stream()
                .map(Player::getColor)
                .collect(Collectors.toList());
        Player player = getPlayer(currentColor);

        if (player.askedForColor()) {
            askedColors.add(player.getColor());
            return;
        }

        Move move = player.makeMove();
        if (move == null && settings.waitUntilMakeAMove()) {
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
        if (aliveBeforeTick.size() > 1) {
            checkVictory();
        }
        checkStalemate();
    }

    @Override
    public void newGame(Player player) {
        if (players.contains(player)) {
            LOGGER.warn("Trying to add player who is already in game");
            return;
        }
        if (players.size() == getColors().size()) {
            LOGGER.warn("Trying to add new player, but the game is already full");
            return;
        }
        if (!players.contains(player)) {
            players.add(player);
        }
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
    public com.codenjoy.dojo.services.printer.BoardReader reader() {
        return new BoardReader(this);
    }

    public int getBoardSize() {
        return board.getSize();
    }

    public Rotator getRotator() {
        return rotator;
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

    // TODO test me
    private void checkStalemate() {
        List<Color> marked = Lists.newArrayList();
        while (currentColor != null && board.getAvailableMoves(currentColor).isEmpty()) {
            if (settings.waitUntilMakeAMove()) {
                getPlayer(currentColor).gameOver();
            } else {
                if (marked.contains(currentColor)) {
                    marked.forEach(c -> getPlayer(c).gameOver());
                } else {
                    marked.add(currentColor);
                    getPlayer(currentColor).event(Events.WRONG_MOVE);
                }
            }
            currentColor = nextColor();
        }
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
            alivePlayers.get(0).win();
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

    public static Direction getDefaultAttackDirection() {
        return DEFAULT_ATTACK_DIRECTION;
    }

    private Color nextColor() {
        List<Player> alivePlayers = getAlivePlayers();
        if (alivePlayers.isEmpty()) {
            return null;
        }
        alivePlayers.sort(Comparator.comparingInt(p -> p.getColor().getPriority()));
        return alivePlayers.stream()
                .map(Player::getColor)
                .filter(color -> color.getPriority() > currentColor.getPriority())
                .findAny()
                .orElse(alivePlayers.get(0).getColor());
    }
}
package com.codenjoy.dojo.chess.service;

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

import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.Events;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessPlayerHero extends PlayerHero<Chess> implements NoDirectionsJoystick {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChessPlayerHero.class);

    private final Color color;

    private final List<Events> events = new ArrayList<>();
    private Move lastMove;
    private Move action;

    private boolean askedForColor = false;

    public ChessPlayerHero(Color color, Chess field) {
        this.color = color;
        this.field = field;
    }

    @Override
    public void act(int... codes) {
        Marker marker = MarkerFactory.getMarker("CHESS_PLAYER_HERO__ACT");
        LOGGER.debug(marker, "Act started with codes {}", Arrays.toString(codes));
        if (field.getCurrentColor() != getColor()) {
            LOGGER.debug(marker,
                    "Player with color {} tried to send an act command when active color was {}",
                    getColor(), field.getCurrentColor()
            );
            return;
        }
        if (codes.length == 0) {
            LOGGER.debug(marker, "{} player is asking for color", color);
            askedForColor = true;
            return;
        }
        action = Move.decode(codes);
        if (action == null) {
            LOGGER.debug(marker,
                    "Hero with color {} received invalid action parameters: {}",
                    getColor(), Arrays.toString(codes)
            );
            return;
        }
        LOGGER.debug(marker, "{} received; preparing for mapping...", action);
        action = field.getRotator()
                .mapMove(color, action);
        LOGGER.debug(marker, "Action after mapping: {}", action);
        LOGGER.debug(marker, "Act ended");
    }

    private void clearBeforeTick() {
        events.clear();
        lastMove = null;
    }

    @Override
    public void tick() {
        Marker marker = MarkerFactory.getMarker("CHESS_PLAYER_HERO__TICK");
        LOGGER.debug(marker, "Tick started for player with {} color", color);
        if (askedForColor) {
            LOGGER.debug(marker, "Player asked for color");
            return;
        }
        clearBeforeTick();
        if (color != field.getCurrentColor()) {
            LOGGER.debug(marker, "{} player trying to make a move, but current player is {}", color, field.getCurrentColor());
            return;
        }
        if (action == null) {
            LOGGER.debug(marker, "{} player didn't respond", color);
            return;
        }
        if (field.getBoard().tryMove(color, action)) {
            lastMove = action;
            LOGGER.debug(marker, "{} successful", action);
        } else {
            LOGGER.debug(marker, "{} unsuccessful", action);
            events.add(Events.WRONG_MOVE);
        }
        action = null;
        LOGGER.debug(marker, "Tick ended");
    }

    public Color getColor() {
        return color;
    }

    public List<Events> getEvents() {
        return events;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public boolean isAlive() {
        return field.getBoard().isAlive(color);
    }

    public boolean isWinner() {
        return field.getBoard().isWinner(color);
    }

    public boolean askedForColor() {
        return askedForColor;
    }

    public void colorsAnswered() {
        askedForColor = false;
    }
}

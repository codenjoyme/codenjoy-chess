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


import com.codenjoy.dojo.chess.service.Event;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

public class Player extends GamePlayer<GameSet, Board> {

    private GameSet gameSet;
    private boolean alive;
    private boolean winner;

    public Player(EventListener listener, GameSettings settings) {
        super(listener, settings);
    }

    public GameSet getGameSet() {
        return gameSet;
    }

    public Color getColor() {
        return gameSet.getColor();
    }

    @Override
    public GameSet getHero() {
        return gameSet; // TODO implement me
    }

    @Override
    public void newHero(Board board) {
        gameSet = board.newGameSet();
        alive = true;
        winner = false;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public boolean isWin() {
        return winner;
    }

    @Override
    public void event(Object eventObj) {
        super.event(eventObj);
        Event event = (Event) eventObj;
        switch (event) {
            case WIN:
                winner = true;
                break;
            case GAME_OVER:
                winner = false;
                alive = false;
                break;
        }
    }

    public Move makeMove() {
        gameSet.tick();
        if (gameSet.isTriedWrongMove()) {
            listener.event(Event.WRONG_MOVE);
        }
        return gameSet.getLastMove();
    }
}

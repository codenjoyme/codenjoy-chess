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


import com.codenjoy.dojo.chess.service.Events;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

import static com.codenjoy.dojo.chess.service.GameSettings.Option.LAST_PLAYER_STAYS;

public class Player extends GamePlayer<Hero, Field> {

    private boolean winner;
    private boolean active;

    public Player(EventListener listener, GameSettings settings) {
        super(listener, settings);
    }

    @Override
    public Hero createHero(Point pt) {
        this.active = true;
        this.winner = false;
        return new Hero(field.getAvailableColor());
    }

    /**
     * The player is alive as long as his king is alive and he
     * has something to attack on the field.
     */
    @Override
    public boolean isAlive() {
        return isActive() && !isLastWinnerOnBoard();
    }

    /**
     * The player is active as long as his king is alive.
     */
    public boolean isActive() {
        return active && hero.isAlive();
    }

    /**
     * @return true - when the player removes all the kings from the field
     *          AND there are no more pieces of a different color.
     */
    public boolean isLastWinnerOnBoard() {
        return isWin() && hero.isWinner();
    }

    /**
     * @return true - when the player removes all the kings from the field
     */
    @Override
    public boolean isWin() {
        return winner;
    }

    @Override
    public boolean shouldLeave() {
        return true;
    }

    public Color getColor() {
        return hero == null ? null : hero.getColor();
    }

    public Move makeMove() {
        hero.tick();
        hero.getEvents().forEach(this::event);
        return hero.getLastMove();
    }

    public boolean askedForColor() {
        return hero.askedForColor();
    }

    @Override
    public boolean wantToStay() {
        return settings.bool(LAST_PLAYER_STAYS) &&
                field.getBoard().getPieces().stream()
                        .anyMatch(p -> !p.getColor().equals(hero.getColor()));
    }

    public void win() {
        event(Events.WIN);
        winner = true;
    }

    public void gameOver() {
        event(Events.GAME_OVER);
        active = false;
    }
}

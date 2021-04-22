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


import com.codenjoy.dojo.chess.model.Events;
import com.codenjoy.dojo.services.PlayerScores;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.codenjoy.dojo.chess.model.Events.*;
import static com.codenjoy.dojo.chess.service.GameSettings.Option.*;

public class Scores implements PlayerScores {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Map<Events, Integer> eventsToRewards;
    private final static int DEFAULT_SCORE = 0;

    private final AtomicInteger score;

    public Scores(GameSettings settings) {
        this(DEFAULT_SCORE, settings);
    }

    public Scores(int startScore, GameSettings settings) {
        this.score = new AtomicInteger(startScore);
        this.eventsToRewards = new HashMap<>() {{
           put(WIN, settings.integer(VICTORY_REWARD));
           put(WRONG_MOVE, settings.integer(WRONG_MOVE_PENALTY) * -1);
           put(GAME_OVER, settings.integer(GAME_OVER_PENALTY) * -1);

           put(KING_TAKEN, settings.integer(KING_WORTH));
           put(QUEEN_TAKEN, settings.integer(QUEEN_WORTH));
           put(BISHOP_TAKEN, settings.integer(BISHOP_WORTH));
           put(ROOK_TAKEN, settings.integer(ROOK_WORTH));
           put(KNIGHT_TAKEN, settings.integer(KNIGHT_WORTH));
           put(PAWN_TAKEN, settings.integer(PAWN_WORTH));
        }};
    }

    @Override
    public int clear() {
        score.set(DEFAULT_SCORE);
        return score.get();
    }

    @Override
    public Integer getScore() {
        return score.get();
    }

    @Override
    public void update(Object score) {
        this.score.set(Integer.parseInt(score.toString()));
        setZeroIfNegative();
    }

    @Override
    public void event(Object eventObj) {
        Events event = (Events) eventObj;
        score.addAndGet(eventsToRewards.get(event));
        setZeroIfNegative();
    }

    private void setZeroIfNegative() {
        if (score.get() < 0) {
            score.set(0);
        }
    }
}
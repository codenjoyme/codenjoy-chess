package com.codenjoy.dojo.chess.services;

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


import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.chess.service.Scores;
import com.codenjoy.dojo.services.PlayerScores;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.chess.service.Events.*;
import static com.codenjoy.dojo.chess.model.item.piece.Piece.Type.KING;
import static com.codenjoy.dojo.chess.model.item.piece.Piece.Type.QUEEN;
import static com.codenjoy.dojo.chess.service.GameSettings.Option.WRONG_MOVE_PENALTY;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    @Before
    public void setup() {
        settings = new GameSettings();
        scores = new Scores(settings);
    }

    @Test
    public void shouldCollectScores() {

        // when
        scores.event(QUEEN_TAKEN);
        scores.event(KING_TAKEN);
        scores.event(WIN);

        // then
        assertEquals(settings.worthOf(QUEEN) + settings.worthOf(KING) + settings.victoryReward(), scores.getScore());
    }

    @Test
    public void shouldClearScore() {

        // given
        scores.event(WIN);

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldNotBeLessThanZero() {

        // given
        settings.integer(WRONG_MOVE_PENALTY, 100);

        // when
        scores.event(WRONG_MOVE);
        scores.event(WRONG_MOVE);
        scores.event(WRONG_MOVE);
        scores.event(WRONG_MOVE);
        scores.event(WRONG_MOVE);

        // then
        assertEquals(0, scores.getScore());
    }
}

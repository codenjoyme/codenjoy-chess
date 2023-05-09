package com.codenjoy.dojo.chess.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.chess.TestGameSettings;
import com.codenjoy.dojo.chess.service.Event;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.chess.service.Scores;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.chess.service.GameSettings.Keys.*;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings();
    }

    @Override
    protected Class<? extends ScoresMap> scores() {
        return Scores.class;
    }

    @Override
    protected Class<? extends Enum> eventTypes() {
        return Event.class;
    }

    @Test
    public void shouldCollectScores() {
        assertEvents("100:\n" +
                "QUEEN_TAKEN > +9 = 109\n" +
                "KING_TAKEN > +20 = 129\n" +
                "WIN > +100 = 229");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("0:\n" +
                "WIN > +100 = 100\n" +
                "(CLEAN) > -100 = 0\n" +
                "WIN > +100 = 100");
    }

    @Test
    public void shouldNotBeLessThanZero() {
        // given
        settings.integer(WRONG_MOVE_PENALTY, -50);

        // when then
        assertEvents("100:\n" +
                "WRONG_MOVE > -50 = 50\n" +
                "WRONG_MOVE > -50 = 0\n" +
                "WRONG_MOVE > +0 = 0\n" +
                "WRONG_MOVE > +0 = 0\n" +
                "WRONG_MOVE > +0 = 0");
    }

    @Test
    public void shouldVictoryScore() {
        // given
        settings.integer(WIN_SCORE, 100);

        // when then
        assertEvents("100:\n" +
                "WIN > +100 = 200\n" +
                "WIN > +100 = 300");
    }

    @Test
    public void shouldWrongMove() {
        // given
        settings.integer(WRONG_MOVE_PENALTY, -10);

        // when then
        assertEvents("100:\n" +
                "WRONG_MOVE > -10 = 90\n" +
                "WRONG_MOVE > -10 = 80");
    }

    @Test
    public void shouldGameOver() {
        // given
        settings.integer(GAME_OVER_PENALTY, -10);

        // when then
        assertEvents("100:\n" +
                "GAME_OVER > -10 = 90\n" +
                "GAME_OVER > -10 = 80");
    }

    @Test
    public void shouldKingTaken() {
        // given
        settings.integer(KING_WORTH, 10);

        // when then
        assertEvents("100:\n" +
                "KING_TAKEN > +10 = 110\n" +
                "KING_TAKEN > +10 = 120");
    }

    @Test
    public void shouldQueenTaken() {
        // given
        settings.integer(QUEEN_WORTH, 10);

        // when then
        assertEvents("100:\n" +
                "QUEEN_TAKEN > +10 = 110\n" +
                "QUEEN_TAKEN > +10 = 120");
    }

    @Test
    public void shouldBishopTaken() {
        // given
        settings.integer(BISHOP_WORTH, 10);

        // when then
        assertEvents("100:\n" +
                "BISHOP_TAKEN > +10 = 110\n" +
                "BISHOP_TAKEN > +10 = 120");
    }

    @Test
    public void shouldRookTaken() {
        // given
        settings.integer(ROOK_WORTH, 10);

        // when then
        assertEvents("100:\n" +
                "ROOK_TAKEN > +10 = 110\n" +
                "ROOK_TAKEN > +10 = 120");
    }

    @Test
    public void shouldKnightTaken() {
        // given
        settings.integer(KNIGHT_WORTH, 10);

        // when then
        assertEvents("100:\n" +
                "KNIGHT_TAKEN > +10 = 110\n" +
                "KNIGHT_TAKEN > +10 = 120");
    }

    @Test
    public void shouldPawnTaken() {
        // given
        settings.integer(PAWN_WORTH, 10);

        // when then
        assertEvents("100:\n" +
                "PAWN_TAKEN > +10 = 110\n" +
                "PAWN_TAKEN > +10 = 120");
    }
}
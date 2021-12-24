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


import com.codenjoy.dojo.chess.TestGameSettings;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.chess.service.Scores;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.item.piece.Piece.Type.*;
import static com.codenjoy.dojo.chess.service.Event.*;
import static com.codenjoy.dojo.chess.service.GameSettings.Option.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(0);

        // when
        scores.event(QUEEN_TAKEN);
        scores.event(KING_TAKEN);
        scores.event(WIN);

        // then
        assertEquals(settings.worthOf(QUEEN)
                + settings.worthOf(KING)
                + settings.victoryScore(),
                scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);
        scores.event(WIN);
        assertEquals(100, scores.getScore());

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldNotBeLessThanZero() {
        // given
        settings.integer(WRONG_MOVE_PENALTY, -100);
        givenScores(100);

        // when
        scores.event(WRONG_MOVE);
        scores.event(WRONG_MOVE);
        scores.event(WRONG_MOVE);
        scores.event(WRONG_MOVE);
        scores.event(WRONG_MOVE);

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldVictoryScore() {
        // given
        settings.integer(WIN_SCORE, 100);
        givenScores(100);

        // when
        scores.event(WIN);
        scores.event(WIN);

        // then
        assertEquals(100
                    + 2 * settings.victoryScore(),
                scores.getScore());
    }

    @Test
    public void shouldWrongMove() {
        // given
        settings.integer(WRONG_MOVE_PENALTY, -10);
        givenScores(100);

        // when
        scores.event(WRONG_MOVE);
        scores.event(WRONG_MOVE);

        // then
        assertEquals(100
                    + 2 * settings.wrongMovePenalty(),
                scores.getScore());
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Scores(settings));
    }

    @Test
    public void shouldGameOver() {
        // given
        settings.integer(GAME_OVER_PENALTY, -10);
        givenScores(100);

        // when
        scores.event(GAME_OVER);
        scores.event(GAME_OVER);

        // then
        assertEquals(100
                    + 2 * settings.gameOverPenalty(),
                scores.getScore());
    }

    @Test
    public void shouldKingTaken() {
        // given
        settings.integer(KING_WORTH, 10);
        givenScores(100);

        // when
        scores.event(KING_TAKEN);
        scores.event(KING_TAKEN);

        // then
        assertEquals(100
                    + 2 * settings.worthOf(KING),
                scores.getScore());
    }

    @Test
    public void shouldQueenTaken() {
        // given
        settings.integer(QUEEN_WORTH, 10);
        givenScores(100);

        // when
        scores.event(QUEEN_TAKEN);
        scores.event(QUEEN_TAKEN);

        // then
        assertEquals(100
                    + 2 * settings.worthOf(QUEEN),
                scores.getScore());
    }

    @Test
    public void shouldBishopTaken() {
        // given
        settings.integer(BISHOP_WORTH, 10);
        givenScores(100);

        // when
        scores.event(BISHOP_TAKEN);
        scores.event(BISHOP_TAKEN);

        // then
        assertEquals(100
                    + 2 * settings.worthOf(BISHOP),
                scores.getScore());
    }

    @Test
    public void shouldRookTaken() {
        // given
        settings.integer(ROOK_WORTH, 10);
        givenScores(100);

        // when
        scores.event(ROOK_TAKEN);
        scores.event(ROOK_TAKEN);

        // then
        assertEquals(100
                    + 2 * settings.worthOf(ROOK),
                scores.getScore());
    }

    @Test
    public void shouldKnightTaken() {
        // given
        settings.integer(KNIGHT_WORTH, 10);
        givenScores(100);

        // when
        scores.event(KNIGHT_TAKEN);
        scores.event(KNIGHT_TAKEN);

        // then
        assertEquals(100
                    + 2 * settings.worthOf(KNIGHT),
                scores.getScore());
    }

    @Test
    public void shouldPawnTaken() {
        // given
        settings.integer(PAWN_WORTH, 10);
        givenScores(100);

        // when
        scores.event(PAWN_TAKEN);
        scores.event(PAWN_TAKEN);

        // then
        assertEquals(100
                    + 2 * settings.worthOf(PAWN),
                scores.getScore());
    }
}
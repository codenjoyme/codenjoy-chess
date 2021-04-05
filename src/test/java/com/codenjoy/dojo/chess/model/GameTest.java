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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameTest extends AbstractGameTest {

    @Test
    public void ShouldProperlyDrawBoard() {

        // when given
        classicBoardAnd2Players();

        // then
        assertE("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
    }

    @Test
    public void ShouldFireWrongMoveEvent_WhenTryingToMakeWrongMove() {

        // given
        classicBoardAnd2Players();

        // when
        move(whitePlayer, Move.from(4, 1).to(5, 3));

        // then
        fired(whiteListener, Event.WRONG_MOVE);
    }

    @Test
    public void firstPlayerShouldBeWhite_andSecondShouldBeBlack() {

        // when given
        classicBoardAnd2Players();

        // then
        assertEquals(Color.WHITE, whitePlayer.getHero().getColor());
        assertEquals(Color.BLACK, blackPlayer.getHero().getColor());
    }

    @Test
    public void shouldNotRenderDeadPieces() {
        // given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....p..." +
                "PPPPPPPP" +
                "RKBQWBKR");
        twoPlayers();

        // when
        move(whitePlayer, Move.from(5, 1).to(4, 2));
        move(blackPlayer, Move.from(0, 6).to(0, 5));
        move(whitePlayer, Move.from(4, 2).to(4, 3));

        // then
        assertE("rkbqwbkr" +
                ".ppp.ppp" +
                "p......." +
                "........" +
                "....P..." +
                "........" +
                "PPPPP.PP" +
                "RKBQWBKR");
    }
}
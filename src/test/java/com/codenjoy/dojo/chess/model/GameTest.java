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


import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;

public class GameTest extends AbstractGameTest {

    @Test
    public void ShouldProperlyDrawBoard() {

        // when given
        classicBoard();

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
        classicBoard();

        // when
        move(WHITE, from(4, 1).to(5, 3));

        // then
        fired(WHITE, WRONG_MOVE);
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

        // when
        move(WHITE, from(5, 1).to(4, 2));
        move(BLACK, from(0, 6).to(0, 5));
        move(WHITE, from(4, 2).to(4, 3));

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
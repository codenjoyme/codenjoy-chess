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
        move(player1, Move.decode(4, 1).to(5, 3));

        // then
        fired(listener1, Event.WRONG_MOVE);
    }
}
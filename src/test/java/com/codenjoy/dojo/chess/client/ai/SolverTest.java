package com.codenjoy.dojo.chess.client.ai;

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

import com.codenjoy.dojo.chess.client.Board;
import com.codenjoy.dojo.chess.client.Color;
import com.codenjoy.dojo.chess.client.ElementsMapper;
import com.codenjoy.dojo.chess.model.Elements;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SolverTest extends AbstractClientTest {
    private static final String REQUEST_FOR_COLOR = "ACT";

    private final Dice dice = mock(Dice.class);
    private Solver<Board> solver;

    @Before
    public void setup() {
        solver = new AISolver(dice);
    }

    @SuppressWarnings("SameParameterValue")
    private String getAnswerFor(Color color) {
        solver.get(board);
        Elements piece = ElementsMapper.getElements(color).get(0);
        return solver.get((Board) new Board().forString(piece.toString()));
    }

    @Test
    public void shouldAskColor_whenNewGameStarts() {

        // when
        given(classicBoard());

        // then
        assertEquals(REQUEST_FOR_COLOR, solver.get(board));
    }

    @Test
    public void shouldAttack_ifPossible() {

        // when
        given(  "w.p" +
                "..." +
                "W.R");

        // then
        assertEquals("ACT(2,0,2,2)", getAnswerFor(Color.WHITE));
    }

    @Test
    public void shouldMakeRandomMove_ifNothingToAttack() {

        // when
        given(  "w.." +
                "..." +
                "W.R");

        // then
        assertEquals("ACT(0,0,0,1)", getAnswerFor(Color.WHITE));
    }
}

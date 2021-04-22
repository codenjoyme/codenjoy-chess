package com.codenjoy.dojo.chess.client.ai;

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

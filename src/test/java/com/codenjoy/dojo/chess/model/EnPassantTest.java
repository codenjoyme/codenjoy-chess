package com.codenjoy.dojo.chess.model;

import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;

// Wiki: https://en.wikipedia.org/wiki/En_passant
public class EnPassantTest extends AbstractGameTest {

    @Test
    public void ShouldBeAllowed_WhenEnemyPawnSteps2SquaresForwardAndOurPawnCanImmediatelyAttackMiddleCellOfTheMove() {

        // given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "....p..." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        move(WHITE, from(5, 1).to(5, 3));

        // when
        move(BLACK, from(4, 3).to(5, 2));

        // then
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                ".....p.." +
                "PPPPP.PP" +
                "RKBQWBKR");
        neverFired(BLACK, WRONG_MOVE);
    }

    @Test
    public void ShouldWorkOnlyWithPawns() {

        // given
        givenFl("rkb.wbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "....q..." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        move(WHITE, from(5, 1).to(5, 3));

        // when
        move(BLACK, from(4, 3).to(5, 2));

        // then
        assertE("rkb.wbkr" +
                "pppppppp" +
                "........" +
                "........" +
                ".....P.." +
                ".....q.." +
                "PPPPP.PP" +
                "RKBQWBKR");
    }

    @Test
    public void ShouldNotBeAllowed_IfHasNotDoneImmediately() {

        // given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "....p..." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        move(WHITE, from(5, 1).to(5, 3));
        move(BLACK, from(0, 6).to(0, 5));
        move(WHITE, from(7, 1).to(7, 2));

        assertE("rkbqwbkr" +
                ".ppp.ppp" +
                "p......." +
                "........" +
                "....pP.." +
                ".......P" +
                "PPPPP.P." +
                "RKBQWBKR");

        // when trying to make "en passant"
        move(BLACK, from(4, 3).to(5, 2));

        // then
        fired(BLACK, WRONG_MOVE);
    }
}

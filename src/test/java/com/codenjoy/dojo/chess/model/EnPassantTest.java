package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.service.Event;
import org.junit.Test;

public class EnPassantTest extends AbstractGameTest {

    // https://en.wikipedia.org/wiki/En_passant
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
        twoPlayers();
        move(whitePlayer, Move.from(5, 1).to(5, 3));

        // when
        move(blackPlayer, Move.from(4, 3).to(5, 2));

        // then
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                ".....p.." +
                "PPPPP.PP" +
                "RKBQWBKR");
        neverFired(blackListener, Event.WRONG_MOVE);
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
        twoPlayers();
        move(whitePlayer, Move.from(5, 1).to(5, 3));

        // when
        move(blackPlayer, Move.from(4, 3).to(5, 2));

        // then
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "....qP.." +
                "........" +
                "PPPPP.PP" +
                "RKBQWBKR");
        fired(blackListener, Event.WRONG_MOVE);
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
        twoPlayers();
        move(whitePlayer, Move.from(5, 1).to(5, 3));
        move(blackPlayer, Move.from(0, 6).to(0, 5));
        move(whitePlayer, Move.from(7, 1).to(7, 2));

        assertE("rkbqwbkr" +
                ".ppp.ppp" +
                "p......." +
                "........" +
                "....pP.." +
                ".......p" +
                "PPPPP.P." +
                "RKBQWBKR");

        // when trying to make "en passant"
        move(whitePlayer, Move.from(4, 3).to(5, 2));

        // then
        fired(whiteListener, Event.WRONG_MOVE);
    }
}

package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.AbstractGameTest;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.service.Event;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class PawnTest extends AbstractGameTest {

    @Test
    public void ShouldBeAbleToWalkForward() {

        // given
        classicBoardAnd2Players();

        // when
        move(whitePlayer, Move.from(4, 1).to(4, 2));
        move(blackPlayer, Move.from(0, 6).to(0, 5));
        move(whitePlayer, Move.from(4, 2).to(4, 3));
        move(blackPlayer, Move.from(3, 6).to(3, 5));

        // then
        neverFired(whiteListener, Event.WRONG_MOVE);
        neverFired(blackListener, Event.WRONG_MOVE);
    }

    @Test
    public void ShouldBeAbleToWalkTwoCellsForward_FromStartPosition() {

        // given
        classicBoardAnd2Players();

        // when
        move(whitePlayer, Move.from(4, 1).to(4, 3));

        // then
        assertE("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "....P..." +
                "........" +
                "PPPP.PPP" +
                "RKBQWBKR");

        neverFired(whiteListener, Event.WRONG_MOVE);
    }

    @Test
    public void ShouldBeAbleToWalkDiagonallyAndGetEnemyPiece_IfThereIsEnemyPiece() {

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
        Piece enemyPawn = blackPlayer.getHero().getPieceAt(4, 2).orElse(null);

        // when
        move(whitePlayer, Move.from(5, 1).to(4, 2));

        // then
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....P..." +
                "PPPPP.PP" +
                "RKBQWBKR");
        assertFalse(enemyPawn.isAlive());
        neverFired(whiteListener, Event.WRONG_MOVE);
    }

    @Test
    public void ShouldNotBeAbleToWalkTwoCellsForward_IfThereIsAnotherPieceOnTheWay() {

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
        move(whitePlayer, Move.from(4, 1).to(4, 3));

        // then
        fired(whiteListener, Event.WRONG_MOVE);
    }

    @Test
    public void ShouldNotBeAbleToWalkTwoCellsForward_IfThereIsAnotherPieceAtTargetSquare() {

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

        // when
        move(whitePlayer, Move.from(4, 1).to(4, 3));

        // then
        fired(whiteListener, Event.WRONG_MOVE);
    }

    @Test
    public void ShouldNotBeAbleToWalkTwoCellsForward_IfNotStaysOnStartPosition() {

        // given
        classicBoardAnd2Players();

        // when
        move(whitePlayer, Move.from(4, 1).to(4, 2));
        move(blackPlayer, Move.from(3, 6).to(3, 5));
        move(whitePlayer, Move.from(4, 2).to(4, 4)); // trying move two cells forward not from start position

        // then
        fired(whiteListener, Event.WRONG_MOVE);
    }

    @Test
    public void ShouldBeAbleToGoDown_WhenPlaysBlack() {

        // given
        classicBoardAnd2Players();

        // when
        move(whitePlayer, Move.from(4, 1).to(4, 2));
        move(blackPlayer, Move.from(3, 6).to(3, 5));

        // then
        neverFired(blackListener, Event.WRONG_MOVE);
    }
}
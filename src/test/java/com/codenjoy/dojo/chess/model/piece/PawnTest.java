package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.AbstractGameTest;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;

public class PawnTest extends AbstractGameTest {

    @Test
    public void ShouldBeAbleToWalkForward() {

        // given
        classicBoard();

        // when
        move(WHITE, from(4, 1).to(4, 2));
        move(BLACK, from(0, 6).to(0, 5));
        move(WHITE, from(4, 2).to(4, 3));
        move(BLACK, from(3, 6).to(3, 5));

        // then
        neverFired(WHITE, WRONG_MOVE);
        neverFired(BLACK, WRONG_MOVE);
    }

    @Test
    public void ShouldBeAbleToWalkTwoCellsForward_FromStartPosition() {

        // given
        classicBoard();

        // when
        move(WHITE, from(4, 1).to(4, 3));

        // then
        assertE("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "....P..." +
                "........" +
                "PPPP.PPP" +
                "RKBQWBKR");
        neverFired(WHITE, WRONG_MOVE);
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
        Piece enemyPawn = getGameSet(BLACK).getPieceAt(4, 2).orElse(null);

        // when
        move(WHITE, from(5, 1).to(4, 2));

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
        neverFired(WHITE, WRONG_MOVE);
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

        // when
        move(WHITE, from(4, 1).to(4, 3));

        // then
        fired(WHITE, WRONG_MOVE);
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

        // when
        move(WHITE, from(4, 1).to(4, 3));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void ShouldNotBeAbleToWalkForward_IfThereIsAnotherPieceAtTargetSquare() {

        // given
        givenFl("r.bqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "....k..." +
                "PPPPPPPP" +
                "RKBQWBKR");

        // when
        move(WHITE, from(4, 1).to(4, 2));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void ShouldNotBeAbleToWalkTwoCellsForward_IfNotStaysOnStartPosition() {

        // given
        classicBoard();

        // when
        move(WHITE, from(4, 1).to(4, 2));
        move(BLACK, from(3, 6).to(3, 5));
        move(WHITE, from(4, 2).to(4, 4)); // trying move two cells forward not from start position

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void ShouldBeAbleToGoDown_WhenPlaysBlack() {

        // given
        classicBoard();

        // when
        move(WHITE, from(4, 1).to(4, 2));
        move(BLACK, from(3, 6).to(3, 5));

        // then
        neverFired(BLACK, WRONG_MOVE);
    }
}
package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.Element;
import com.codenjoy.dojo.services.PointImpl;
import org.fest.util.Arrays;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PawnTest extends AbstractPieceTest {

    public PawnTest() {
        super(Element.WHITE_PAWN);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

        // given
        classicBoard();
        Piece whitePawn = getPieceAt(4, 1);

        assertCanMoveOnlyTo(whitePawn,
                new PointImpl(4, 2),
                new PointImpl(4, 3)
        );
    }

    @Override
    public void shouldBeAbleToTakeEnemyPiece() {

        // given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....p..." +
                ".....P.." +
                "RKBQWBKR");
        Piece blackPawn = getPieceAt(4 ,2);

        // when
        move(WHITE, from(5, 1).to(4, 2));

        // then
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....P..." +
                "........" +
                "RKBQWBKR");
        assertFalse(blackPawn.isAlive());
        neverFired(WHITE, WRONG_MOVE);
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        // given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....Q..." +
                ".....P.." +
                "RKB.WBKR");
        Piece whiteQueen = getPieceAt(4 ,2);

        // when
        move(WHITE, from(5, 1).to(4, 2));

        // then
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....Q..." +
                ".....P.." +
                "RKB.WBKR");
        assertTrue(whiteQueen.isAlive());
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMoveSomewhere_ifThereIsEnemyPieceOnTheWay() {

        // when given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....p..." +
                "PPPPPPPP" +
                "RKBQWBKR");
        Piece whitePawn = getPieceAt(4, 1);

        // then
        assertCanMoveOnlyTo(whitePawn, Arrays.array());
    }

    @Test
    public void shouldNotBeAbleToMoveSomewhere_ifThereIsFriendlyPieceOnTheWay() {

        // when given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....Q..." +
                "PPPPPPPP" +
                "RKB.WBKR");
        Piece whitePawn = getPieceAt(4, 1);

        // then
        assertCanMoveOnlyTo(whitePawn, Arrays.array());
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForward_ifThereIsAnotherPieceAtTheTargetPosition() {

        // when given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "....p..." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        Piece whitePawn = getPieceAt(4, 1);

        // then
        assertCanMoveOnlyTo(whitePawn, new PointImpl(4, 2));
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForward_ifNotAtStartPosition() {

        // given
        classicBoard();
        Piece whitePawn = getPieceAt(4, 1);

        // when
        Preconditions preconditions = () -> {
            move(WHITE, from(4, 1).to(4, 2));
            move(BLACK, from(3, 6).to(3, 5));
        };

        // then
        assertCanMoveOnlyTo(preconditions, whitePawn, new PointImpl(4, 3));
    }

    @Test
    public void blackPawnsShouldBeAbleToMoveDown() {

        // given
        classicBoard();
        Piece blackPawn = getPieceAt(3, 6);

        // when
        Preconditions preconditions = () -> move(WHITE, from(4, 1).to(4, 2));

        // then
        assertCanMoveOnlyTo(preconditions, blackPawn,
                new PointImpl(3, 5),
                new PointImpl(3, 4)
        );
    }
}
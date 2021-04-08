package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.Element;
import com.codenjoy.dojo.services.PointImpl;
import org.fest.util.Arrays;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KingTest extends AbstractPieceTest {

    public KingTest() {
        super(Element.WHITE_KING);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

        // when
        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...W...." +
                "........" +
                "........" +
                "........");
        Piece whiteKing = getPieceAt(3, 3);

        // then
        assertCanMoveOnlyTo(whiteKing,
                new PointImpl(2, 2),
                new PointImpl(2, 3),
                new PointImpl(2, 4),
                new PointImpl(3, 2),
                new PointImpl(3, 4),
                new PointImpl(4, 2),
                new PointImpl(4, 3),
                new PointImpl(4, 4)
        );
    }

    @Override
    public void shouldBeAbleToTakeEnemyPiece() {

        // when
        givenFl("w......." +
                "........" +
                "........" +
                "....p..." +
                "...W...." +
                "........" +
                "........" +
                "........");
        Piece blackPawn = getPieceAt(4, 4);

        // when
        move(WHITE, from(3, 3).to(4, 4));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "....W..." +
                "........" +
                "........" +
                "........" +
                "........");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(blackPawn.isAlive());
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        // when
        givenFl("w......." +
                "........" +
                "........" +
                "....Q..." +
                "...W...." +
                "........" +
                "........" +
                "........");
        Piece whiteQueen = getPieceAt(4, 4);

        // when
        move(WHITE, from(3, 3).to(4, 4));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "....Q..." +
                "...W...." +
                "........" +
                "........" +
                "........");
        fired(WHITE, WRONG_MOVE);
        assertTrue(whiteQueen.isAlive());
    }

    @Test
    public void shouldNotBeAbleToMoveOutOfSquaresOnBoard() {

        // when
        givenFl("W");
        Piece whiteKing = getPieceAt(0, 0);

        // then
        assertCanMoveOnlyTo(whiteKing, Arrays.array());
    }
}

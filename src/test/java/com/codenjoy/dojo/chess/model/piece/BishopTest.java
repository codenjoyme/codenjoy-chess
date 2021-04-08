package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.Element;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BishopTest extends AbstractPieceTest {

    public BishopTest() {
        super(Element.WHITE_BISHOP);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");
        Piece whiteBishop = getPieceAt(3, 3);

        assertCanMoveOnlyTo(whiteBishop,
                new PointImpl(0, 0),
                new PointImpl(1, 1),
                new PointImpl(2, 2),
                new PointImpl(4, 4),
                new PointImpl(5, 5),
                new PointImpl(6, 6),
                new PointImpl(7, 7),
                new PointImpl(6, 0),
                new PointImpl(5, 1),
                new PointImpl(4, 2),
                new PointImpl(2, 4),
                new PointImpl(1, 5),
                new PointImpl(0, 6)
        );
    }

    @Override
    public void shouldBeAbleToTakeEnemyPiece() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                "q......W");
        Piece blackQueen = getPieceAt(0, 0);

        // when
        move(WHITE, from(3, 3).to(0, 0));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "B......W");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(blackQueen.isAlive());
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                "Q......W");
        Piece whiteQueen = getPieceAt(0, 0);

        // when
        move(WHITE, from(3, 3).to(0, 0));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                "Q......W");
        fired(WHITE, WRONG_MOVE);
        assertTrue(whiteQueen.isAlive());
    }

    @Test
    public void shouldNotBeAbleToMoveThroughEnemyPiece() {

        givenFl("w......." +
                "........" +
                ".....q.." +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(7, 7));

        // then
        assertE("w......." +
                "........" +
                ".....q.." +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkThroughFriendlyPiece() {

        givenFl("w......." +
                "........" +
                ".....Q.." +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(7, 7));

        // then
        assertE("w......." +
                "........" +
                ".....Q.." +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

}

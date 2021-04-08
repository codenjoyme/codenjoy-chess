package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.Element;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RookTest extends AbstractPieceTest {

    public RookTest() {
        super(Element.WHITE_ROOK);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {
        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...R...." +
                "........" +
                "........" +
                ".......W");
        Piece whiteRook = getPieceAt(3, 3);

        assertCanMoveOnlyTo(whiteRook,
                new PointImpl(0, 3),
                new PointImpl(1, 3),
                new PointImpl(2, 3),
                new PointImpl(0, 3),
                new PointImpl(4, 3),
                new PointImpl(5, 3),
                new PointImpl(6, 3),
                new PointImpl(7, 3),
                new PointImpl(3, 0),
                new PointImpl(3, 1),
                new PointImpl(3, 2),
                new PointImpl(3, 4),
                new PointImpl(3, 5),
                new PointImpl(3, 6),
                new PointImpl(3, 7)
        );
    }

    @Override
    public void shouldBeAbleToTakeEnemyPiece() {

        givenFl("w......." +
                "...q...." +
                "........" +
                "........" +
                "...R...." +
                "........" +
                "........" +
                ".......W");
        Piece enemyQueen = getPieceAt(3, 6);

        // when
        move(WHITE, from(3, 3).to(3, 6));

        // then
        assertE("w......." +
                "...R...." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(enemyQueen.isAlive());
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        givenFl("w......." +
                "...Q...." +
                "........" +
                "........" +
                "...R...." +
                "........" +
                "........" +
                ".......W");
        Piece friendlyQueen = getPieceAt(3, 6);

        // when
        move(WHITE, from(3, 3).to(3, 6));

        // then
        assertE("w......." +
                "...Q...." +
                "........" +
                "........" +
                "...R...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
        assertTrue(friendlyQueen.isAlive());
    }

    @Test
    public void shouldNotBeAbleToMoveThroughEnemyPiece() {
        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...R..q." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(7, 3));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...R..q." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMoveThroughFriendlyPiece() {
        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...R..Q." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(7, 3));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...R..Q." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }
}

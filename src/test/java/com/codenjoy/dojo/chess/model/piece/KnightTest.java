package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.Element;
import com.codenjoy.dojo.services.PointImpl;

import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KnightTest extends AbstractPieceTest {

    public KnightTest() {
        super(Element.WHITE_KNIGHT);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

        // when
        givenFl("w......." +
                "........" +
                "...b...." +
                "..pp...." +
                ".qpKp..." +
                "..ppp..." +
                "........" +
                ".......W");
        Piece whiteKnight = getPieceAt(3, 3);

        // then
        assertCanMoveOnlyTo(whiteKnight,
                new PointImpl(1, 2),
                new PointImpl(1, 4),
                new PointImpl(5, 2),
                new PointImpl(5, 4),
                new PointImpl(2, 5),
                new PointImpl(4, 5),
                new PointImpl(2, 1),
                new PointImpl(4, 1)
        );
    }

    @Override
    public void shouldBeAbleToTakeEnemyPiece() {

        givenFl("w......." +
                "........" +
                "....q..." +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        Piece enemyQueen = getPieceAt(4, 5);

        // when
        move(WHITE, from(3, 3).to(4, 5));

        // then
        assertE("w......." +
                "........" +
                "....K..." +
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
                "........" +
                "....Q..." +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        Piece friendlyQueen = getPieceAt(4, 5);

        // when
        move(WHITE, from(3, 3).to(4, 5));

        // then
        assertE("w......." +
                "........" +
                "....Q..." +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
        assertTrue(friendlyQueen.isAlive());
    }
}

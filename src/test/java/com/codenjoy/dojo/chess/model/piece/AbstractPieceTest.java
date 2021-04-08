package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.AbstractGameTest;
import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.Element;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.google.common.collect.Lists;
import org.fest.util.Arrays;
import org.junit.Test;

import java.util.List;

import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertEquals;

public abstract class AbstractPieceTest extends AbstractGameTest {

    private final Element testingElement;

    protected AbstractPieceTest(Element testingElement) {
        this.testingElement = testingElement;
    }

    @Test
    public void shouldNotBeAbleToMoveOutOfSquaresOnBoard() {

        // when given
        if (testingElement == Element.WHITE_KING) {
            givenFl("W");
        } else {
            givenFl("W " + testingElement.ch());
        }
        Piece piece = getPieceAt(0, 0);

        // then
        assertCanMoveOnlyTo(piece, Arrays.array());
    }

    @Test
    public abstract void shouldMoveInAccordanceWithClassicChessRules();

    @Test
    public abstract void shouldBeAbleToTakeEnemyPiece();

    @Test
    public abstract void shouldNotBeAbleToTakeFriendlyPiece();


    protected void assertCanMoveOnlyTo(Piece piece, Point... positionsArray) {
        assertCanMoveOnlyTo(null, piece, positionsArray);
    }

    protected void assertCanMoveOnlyTo(Preconditions preconditions, Piece piece, Point... positionsArray) {
        List<Point> positions = Lists.newArrayList(positionsArray);
        Color color = piece.getColor();
        for (int x = -1; x <= game.size(); x++) {
            for (int y = -1; y <= game.size(); y++) {
                reset();
                Piece p = getPieceAt(piece.getPosition());
                if (preconditions != null) {
                    preconditions.run();
                }
                assertEquals(color, game.getCurrentColor());
                PointImpl position = new PointImpl(x, y);
                Move move = from(p.getPosition()).to(position);
                try {
                    move(color, move);
                    if (positions.contains(position)) {
                        neverFired(color, WRONG_MOVE);
                    } else {
                        fired(color, WRONG_MOVE);
                    }
                } catch (Throwable ex) {
                    System.err.println(move);
                    throw ex;
                }
            }
        }
        reset();
    }


    protected interface Preconditions extends Runnable {
    }
}


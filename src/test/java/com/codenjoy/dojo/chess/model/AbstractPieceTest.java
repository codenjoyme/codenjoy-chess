package com.codenjoy.dojo.chess.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.games.chess.Element;
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
        HeroColor color = piece.getColor();
        Point position = piece.getPosition();
        for (int x = -1; x <= getBoardSize(); x++) {
            for (int y = -1; y <= getBoardSize(); y++) {
                reset();
                // Should take new piece everytime and not use received one
                // because preconditions can change it's state
                Piece testPiece = getPieceAt(position);
                if (preconditions != null) {
                    preconditions.run();
                    try {
                        neverFired(WRONG_MOVE);
                    } catch (Throwable ex) {
                        System.err.println("Preconditions are invalid:");
                        System.err.println(history.toString());
                        throw ex;
                    }
                }
                try {
                    assertEquals(color, game.getCurrentColor());
                } catch (AssertionError err) {
                    System.err.println(history.toString());
                    throw err;
                }
                PointImpl destination = new PointImpl(x, y);
                Move move = from(testPiece.getPosition()).to(destination);
                try {
                    move(color, move);
                    if (positions.contains(destination)) {
                        neverFired(color, WRONG_MOVE);
                    } else {
                        fired(color, WRONG_MOVE);
                    }
                } catch (Throwable ex) {
                    System.err.println(history.toString());
                    System.err.println("\nBoard: ");
                    System.err.println(getBoardState(color));
                    throw ex;
                }
            }
        }
        reset();
    }


    protected interface Preconditions extends Runnable {
    }
}


package com.codenjoy.dojo.chess.model.item.piece;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.chess.model.Elements;
import com.codenjoy.dojo.chess.common.AbstractPieceTest;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.model.Events.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BishopTest extends AbstractPieceTest {

    public BishopTest() {
        super(Elements.WHITE_BISHOP);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

        givenFl("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...B....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
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

        givenFl("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...B....\n" +
                "........\n" +
                "........\n" +
                "q......W\n");
        Piece blackQueen = getPieceAt(0, 0);

        // when
        move(WHITE, from(3, 3).to(0, 0));

        // then
        assertE("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "B......W\n");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(blackQueen.isAlive());
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        givenFl("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...B....\n" +
                "........\n" +
                "........\n" +
                "Q......W\n");
        Piece whiteQueen = getPieceAt(0, 0);

        // when
        move(WHITE, from(3, 3).to(0, 0));

        // then
        assertE("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...B....\n" +
                "........\n" +
                "........\n" +
                "Q......W\n");
        fired(WHITE, WRONG_MOVE);
        assertTrue(whiteQueen.isAlive());
    }

    @Test
    public void shouldNotBeAbleToMoveThroughEnemyPiece() {

        givenFl("w.......\n" +
                "........\n" +
                ".....q..\n" +
                "........\n" +
                "...B....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");

        // when
        move(WHITE, from(3, 3).to(7, 7));

        // then
        assertE("w.......\n" +
                "........\n" +
                ".....q..\n" +
                "........\n" +
                "...B....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkThroughFriendlyPiece() {

        givenFl("w.......\n" +
                "........\n" +
                ".....Q..\n" +
                "........\n" +
                "...B....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");

        // when
        move(WHITE, from(3, 3).to(7, 7));

        // then
        assertE("w.......\n" +
                "........\n" +
                ".....Q..\n" +
                "........\n" +
                "...B....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        fired(WHITE, WRONG_MOVE);
    }

}

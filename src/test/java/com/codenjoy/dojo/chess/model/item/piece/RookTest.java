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
import static com.codenjoy.dojo.chess.service.Events.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RookTest extends AbstractPieceTest {

    public RookTest() {
        super(Elements.WHITE_ROOK);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {
        givenFl("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...R....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
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

        givenFl("w.......\n" +
                "...q....\n" +
                "........\n" +
                "........\n" +
                "...R....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        Piece enemyQueen = getPieceAt(3, 6);

        // when
        move(WHITE, from(3, 3).to(3, 6));

        // then
        assertE("w.......\n" +
                "...R....\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(enemyQueen.isAlive());
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        givenFl("w.......\n" +
                "...Q....\n" +
                "........\n" +
                "........\n" +
                "...R....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        Piece friendlyQueen = getPieceAt(3, 6);

        // when
        move(WHITE, from(3, 3).to(3, 6));

        // then
        assertE("w.......\n" +
                "...Q....\n" +
                "........\n" +
                "........\n" +
                "...R....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        fired(WHITE, WRONG_MOVE);
        assertTrue(friendlyQueen.isAlive());
    }

    @Test
    public void shouldNotBeAbleToMoveThroughEnemyPiece() {
        givenFl("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...R..q.\n" +
                "........\n" +
                "........\n" +
                ".......W\n");

        // when
        move(WHITE, from(3, 3).to(7, 3));

        // then
        assertE("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...R..q.\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMoveThroughFriendlyPiece() {
        givenFl("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...R..Q.\n" +
                "........\n" +
                "........\n" +
                ".......W\n");

        // when
        move(WHITE, from(3, 3).to(7, 3));

        // then
        assertE("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...R..Q.\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        fired(WHITE, WRONG_MOVE);
    }
}

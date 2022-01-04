package com.codenjoy.dojo.chess.model.item.piece;

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

import com.codenjoy.dojo.games.chess.Element;
import com.codenjoy.dojo.chess.model.AbstractPieceTest;
import com.codenjoy.dojo.services.PointImpl;

import static com.codenjoy.dojo.chess.model.HeroColor.WHITE;
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
        givenFl("w.......\n" +
                "........\n" +
                "...b....\n" +
                "..pp....\n" +
                ".qpKp...\n" +
                "..ppp...\n" +
                "........\n" +
                ".......W\n");
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

        givenFl("w.......\n" +
                "........\n" +
                "....q...\n" +
                "........\n" +
                "...K....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        Piece enemyQueen = getPieceAt(4, 5);

        // when
        move(WHITE, from(3, 3).to(4, 5));

        // then
        assertE("w.......\n" +
                "........\n" +
                "....K...\n" +
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
                "........\n" +
                "....Q...\n" +
                "........\n" +
                "...K....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        Piece friendlyQueen = getPieceAt(4, 5);

        // when
        move(WHITE, from(3, 3).to(4, 5));

        // then
        assertE("w.......\n" +
                "........\n" +
                "....Q...\n" +
                "........\n" +
                "...K....\n" +
                "........\n" +
                "........\n" +
                ".......W\n");
        fired(WHITE, WRONG_MOVE);
        assertTrue(friendlyQueen.isAlive());
    }
}

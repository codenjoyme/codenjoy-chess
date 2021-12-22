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

import com.codenjoy.dojo.chess.model.AbstractPieceTest;
import com.codenjoy.dojo.games.chess.Element;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.HeroColor.WHITE;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueenTest extends AbstractPieceTest {

    public QueenTest() {
        super(Element.WHITE_QUEEN);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...Q...." +
                "........" +
                "........" +
                ".......W");
        Piece whiteQueen = getPieceAt(3, 3);

        assertCanMoveOnlyTo(whiteQueen,
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
                new PointImpl(3, 7),
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
                "...Q....\n" +
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
                "Q......W\n");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(blackQueen.isAlive());
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        givenFl("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...Q....\n" +
                "........\n" +
                "........\n" +
                "B......W\n");
        Piece whiteBishop = getPieceAt(0, 0);

        // when
        move(WHITE, from(3, 3).to(0, 0));

        // then
        assertE("w.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...Q....\n" +
                "........\n" +
                "........\n" +
                "B......W\n");
        fired(WHITE, WRONG_MOVE);
        assertTrue(whiteBishop.isAlive());
    }

    @Test
    public void shouldNotMoveOutOfBounds() {

        // when
        givenFl("w......Q\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...W....\n");
        Piece whiteQueen = getPieceAt(7, 7);

        // then
        assertCanMoveOnlyTo(whiteQueen,
                new PointImpl(0, 7),
                new PointImpl(1, 7),
                new PointImpl(2, 7),
                new PointImpl(3, 7),
                new PointImpl(4, 7),
                new PointImpl(5, 7),
                new PointImpl(6, 7),
                new PointImpl(7, 0),
                new PointImpl(7, 1),
                new PointImpl(7, 2),
                new PointImpl(7, 3),
                new PointImpl(7, 4),
                new PointImpl(7, 5),
                new PointImpl(7, 6),
                new PointImpl(0, 0),
                new PointImpl(1, 1),
                new PointImpl(2, 2),
                new PointImpl(3, 3),
                new PointImpl(4, 4),
                new PointImpl(5, 5),
                new PointImpl(6, 6)
        );
    }
}

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

import com.codenjoy.dojo.chess.common.AbstractPieceTest;
import com.codenjoy.dojo.chess.model.Elements;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

public class QueenTest extends AbstractPieceTest {

    public QueenTest() {
        super(Elements.WHITE_QUEEN);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

    }

    @Override
    public void shouldBeAbleToTakeEnemyPiece() {

    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

    }

    @Test
    public void shouldNotMoveOutOfBounds() {

        // when
        givenFl("w......Q" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "...W....");
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

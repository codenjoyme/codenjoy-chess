package com.codenjoy.dojo.chess.client.ai;

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

import com.codenjoy.dojo.chess.client.Color;
import com.codenjoy.dojo.chess.model.Elements;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoardTest extends AbstractClientTest {

    @Test
    public void shouldWork_getAt() {

        // when
        given(classicFFABoard());

        // then
        assertEquals(Elements.BARRIER, board.getAt(0, 0));
        assertEquals(Elements.WHITE_ROOK, board.getAt(3, 0));
        assertEquals(Elements.SQUARE, board.getAt(3, 3));
        assertEquals(Elements.WHITE_PAWN, board.getAt(3, 1));
    }

    @Test
    public void shouldWork_getBarriers() {

        // when
        given(classicFFABoard());

        Set<PointImpl> barriers = Sets.newHashSet(
                // left down corner
                new PointImpl(0, 0),
                new PointImpl(0, 1),
                new PointImpl(0, 2),
                new PointImpl(1, 0),
                new PointImpl(1, 1),
                new PointImpl(1, 2),
                new PointImpl(2, 0),
                new PointImpl(2, 1),
                new PointImpl(2, 2),
                // left up corner
                new PointImpl(0, 11),
                new PointImpl(0, 12),
                new PointImpl(0, 13),
                new PointImpl(1, 11),
                new PointImpl(1, 12),
                new PointImpl(1, 13),
                new PointImpl(2, 11),
                new PointImpl(2, 12),
                new PointImpl(2, 13),
                // right down corner
                new PointImpl(11, 0),
                new PointImpl(11, 1),
                new PointImpl(11, 2),
                new PointImpl(12, 0),
                new PointImpl(12, 1),
                new PointImpl(12, 2),
                new PointImpl(13, 0),
                new PointImpl(13, 1),
                new PointImpl(13, 2),
                // right up corner
                new PointImpl(11, 11),
                new PointImpl(11, 12),
                new PointImpl(11, 13),
                new PointImpl(12, 11),
                new PointImpl(12, 12),
                new PointImpl(12, 13),
                new PointImpl(13, 11),
                new PointImpl(13, 12),
                new PointImpl(13, 13)
        );

        // then
        assertEquals(barriers, Sets.newHashSet(board.getBarriers()));
    }

    @Test
    public void shouldWork_isBarrier() {

        // when
        given(classicFFABoard());

        // then
        assertTrue(board.isBarrier(11, 11));
        assertTrue(board.isBarrier(new PointImpl(0, 0)));
    }

    @Test
    public void shouldWork_getColor() {

        // when
        given(classicFFABoard());

        // then
        assertEquals(Color.WHITE, board.getColor(5, 0));
        assertEquals(Color.BLACK, board.getColor(4, 12));
        assertEquals(Color.RED, board.getColor(new PointImpl(0, 6)));
        assertEquals(Color.BLUE, board.getColor(new PointImpl(13, 7)));
    }

    @Test
    public void shouldWork_getPieces() {

        // when
        given(classicFFABoard());

        Set<Point> whitePieces = Sets.newHashSet(
                new PointImpl(3, 0),
                new PointImpl(4, 0),
                new PointImpl(5, 0),
                new PointImpl(6, 0),
                new PointImpl(7, 0),
                new PointImpl(8, 0),
                new PointImpl(9, 0),
                new PointImpl(10, 0),
                new PointImpl(3, 1),
                new PointImpl(4, 1),
                new PointImpl(5, 1),
                new PointImpl(6, 1),
                new PointImpl(7, 1),
                new PointImpl(8, 1),
                new PointImpl(9, 1),
                new PointImpl(10, 1)
        );

        // then
        assertEquals(whitePieces, Sets.newHashSet(board.getPieces(Color.WHITE)));
    }
}

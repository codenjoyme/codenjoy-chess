package com.codenjoy.dojo.chess.model;

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

import com.codenjoy.dojo.chess.model.item.piece.Piece;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.HeroColor.*;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;

public class EnPassantFFATest extends AbstractGameTest {

    @Test
    public void shouldBeAllowed_whenBlueAttacksRed() {

        givenFl("rkbqwbkr\n" +
                "pppppppp\n" +
                "........\n" +
                "Y..z...y\n" +
                ".Z......\n" +
                "........\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n");
        Piece redPawn = getPieceAt(1, 3);
        move(WHITE, from(4, 1).to(4, 2));
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(0, 6).to(0, 5));

        // when
        move(BLUE, from(3, 4).to(2, 3));

        // then
        assertE("rkbqwbkr\n" +
                ".ppppppp\n" +
                "p.......\n" +
                "Y......y\n" +
                "..z.....\n" +
                "....P...\n" +
                "PPPP.PPP\n" +
                "RKBQWBKR\n");
        neverFired(BLUE, WRONG_MOVE);
        assertFalse(redPawn.isAlive());
    }

    @Test
    public void shouldBeAllowed_whenRedAttacksBlue() {

        givenFl("rkbqwbkr\n" +
                "pppppppp\n" +
                "........\n" +
                "Y.....zy\n" +
                "...Z....\n" +
                "........\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n");
        Piece bluePawn = getPieceAt(6, 4);
        move(WHITE, from(4, 1).to(4, 2));
        move(RED, from(3, 3).to(4, 3));
        move(BLACK, from(0, 6).to(0, 5));
        move(BLUE, from(6, 4).to(4, 4));
        move(WHITE, from(0, 1).to(0, 2));

        // when
        move(RED, from(4, 3).to(5, 4));

        // then
        assertE("rkbqwbkr\n" +
                ".ppppppp\n" +
                "p.......\n" +
                "Y....Z.y\n" +
                "........\n" +
                "P...P...\n" +
                ".PPP.PPP\n" +
                "RKBQWBKR\n");
        neverFired(BLUE, WRONG_MOVE);
        assertFalse(bluePawn.isAlive());
    }

    @Test
    public void shouldNotBeAllowedFromSide() {

        givenFl("rkbqwbkr\n" +
                "pppppppp\n" +
                "........\n" +
                "Y......y\n" +
                "...Z....\n" +
                "........\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n");
        move(WHITE, from(4, 1).to(4, 3));

        // when trying to do en passant from side
        move(RED, from(3, 3).to(4, 2));

        // then
        assertE("rkbqwbkr\n" +
                "pppppppp\n" +
                "........\n" +
                "Y......y\n" +
                "...ZP...\n" +
                "........\n" +
                "PPPP.PPP\n" +
                "RKBQWBKR\n");
        fired(RED, WRONG_MOVE);
    }
}

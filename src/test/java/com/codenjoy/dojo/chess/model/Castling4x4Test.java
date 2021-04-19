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

import com.codenjoy.dojo.chess.common.AbstractGameTest;
import org.junit.Ignore;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.*;
import static com.codenjoy.dojo.chess.model.Events.WRONG_MOVE;
import static com.codenjoy.dojo.chess.model.Move.from;

@Ignore
public class Castling4x4Test extends AbstractGameTest {

    /**************************
     *  CASTLING NOT ALLOWED  *
     **************************/

    @Test
    public void shouldNotBeAbleToMakeCastling_ifKingAlreadyMoved() {

        givenFl(".w.....i\n" +
                "........\n" +
                "........\n" +
                ".......y\n" +
                "Y.......\n" +
                "........\n" +
                "........\n" +
                "I...W..R\n");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(4, 1).to(4, 0)); // King returns to it's default place
        move(BLACK, from(1, 6).to(1, 7));

        // when try to make castling
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifCastlingAlreadyDoneOnce() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R...W..R\n");
        move(WHITE, from(4, 0).to(0, 0)); // make a castling
        assertE(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "..WR...R\n");
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(2, 0).to(2, 1));
        move(BLACK, from(1, 6).to(1, 7));
        move(WHITE, from(2, 1).to(3, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(3, 1).to(4, 1));
        move(BLACK, from(1, 6).to(1, 7));
        move(WHITE, from(4, 1).to(4, 0)); // King returns on it's default place
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(3, 0).to(0, 0)); // Rook returns on it's default place
        move(BLACK, from(1, 6).to(1, 7));
        assertE(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R...W..R\n");

        // when try to make one more castling
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastlingWithRook_ifTheRookAlreadyMoved() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R...W..R\n");
        move(WHITE, from(0, 0).to(0, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(0, 1).to(0, 0)); // Rook returns on it's default position
        move(BLACK, from(1, 6).to(1, 7));

        // when trying to make castling with rook that already moved
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifThereIsFriendlyPieceBetweenKingAndRook() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R.B.W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifThereIsEnemyPieceBetweenKingAndRook() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R.b.W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        assertE(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R.b.W..R\n");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifKingIsUnderACheck() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "..b.....\n" +
                "........\n" +
                "R...W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeLongCastling_ifKingShouldPassThroughSquareWhichAttackedByEnemy() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "...r....\n" +
                "........\n" +
                "R...W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeShortCastling_ifKingShouldPassThroughSquareWhichAttackedByEnemy() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                ".....r..\n" +
                "........\n" +
                "R...W..R\n");

        // when
        move(WHITE, from(4, 0).to(7, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeLongCastling_ifAfterThatKingWillBeUnderCheck() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "..r.....\n" +
                "........\n" +
                "R...W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeShortCastling_ifAfterThatKingWillBeUnderCheck() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "......r.\n" +
                "........\n" +
                "R...W..R\n");

        // when
        move(WHITE, from(4, 0).to(7, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifRookAndKingIsUnderAttackOfOnePiece() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "q.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R...W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }


    /**************************
     *    CASTLING ALLOWED    *
     **************************/

    @Test
    public void shouldNotBeAbleToMakeCastlingWithEnemyRook() {

        givenFl(".w.....i\n" +
                "........\n" +
                "........\n" +
                ".......y\n" +
                "Y.......\n" +
                "........\n" +
                "........\n" +
                "I...W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        assertE(".w.....i\n" +
                "........\n" +
                "........\n" +
                ".......y\n" +
                "Y.......\n" +
                "........\n" +
                "........\n" +
                "I...W..R\n");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastling_red() {

        givenFl(".w.....i\n" +
                "........\n" +
                "........\n" +
                ".......y\n" +
                "Y.......\n" +
                "........\n" +
                "........\n" +
                "I...W..R\n");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));

        // when
        move(RED, from(0, 3).to(0, 0));

        // then
        assertE(".......i\n" +
                ".w......\n" +
                "........\n" +
                ".......y\n" +
                "........\n" +
                "I.......\n" +
                "Y...W...\n" +
                ".......R\n");
        neverFired(RED, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastling_blue() {

        givenFl(".w.....i\n" +
                "........\n" +
                "........\n" +
                ".......y\n" +
                "Y.......\n" +
                "........\n" +
                "........\n" +
                "I...W...\n");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(RED, from(0, 3).to(0, 0));

        // when
        move(BLUE, from(7, 4).to(7, 7));

        // then
        assertE("........\n" +
                ".w.....y\n" +
                ".......i\n" +
                "........\n" +
                "........\n" +
                "I.......\n" +
                "Y...W...\n" +
                "........\n");
        neverFired(BLUE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifKingIsUnderAttack() {

        givenFl(".w.....i\n" +
                "........\n" +
                "........\n" +
                "R......y\n" +
                "Y.......\n" +
                "........\n" +
                "........\n" +
                "I...W...\n");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(RED, from(0, 3).to(1, 3));

        // when
        move(BLUE, from(7, 4).to(7, 7));

        // then
        assertE(".......i\n" +
                ".w......\n" +
                "........\n" +
                "R......y\n" +
                ".Y......\n" +
                "........\n" +
                "....W...\n" +
                "I.......\n");
        fired(BLUE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastling_ifKingIsNotUnderAttack() {

        givenFl(".w.....i\n" +
                "........\n" +
                "........\n" +
                "I..b...y\n" +
                "Y.......\n" +
                "........\n" +
                "........\n" +
                "I...W...\n");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(RED, from(0, 3).to(0, 0));

        // when
        move(BLUE, from(7, 4).to(7, 7));

        // then
        assertE("........\n" +
                ".w.....y\n" +
                ".......i\n" +
                "I..b....\n" +
                "........\n" +
                "I.......\n" +
                "Y...W...\n" +
                "........\n");
        neverFired(BLUE, WRONG_MOVE);
    }

    @Test
    public void blueShouldBeAbleToMakeCastling_ifUnderRedAttack() {

        givenFl(".w.....i\n" +
                "........\n" +
                "........\n" +
                "I......y\n" +
                "Y.......\n" +
                "........\n" +
                "........\n" +
                "I...W...\n");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(RED, from(0, 3).to(0, 0));

        // when
        move(BLUE, from(7, 4).to(7, 7));

        // then
        assertE("........\n" +
                ".w.....y\n" +
                ".......i\n" +
                "I..b....\n" +
                "........\n" +
                "I.......\n" +
                "Y...W...\n" +
                "........\n");
        neverFired(BLUE, WRONG_MOVE);
    }
}

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

import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Events.WRONG_MOVE;

public class CastlingTest extends AbstractGameTest {

    /**************************
     *  CASTLING NOT ALLOWED  *
     **************************/

    @Test
    public void shouldNotBeAbleToMakeCastling_ifKingAlreadyMoved() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R...W..R\n");
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
    public void shouldNotBeAbleToMakeCastling_ifKingIsUnderCheck() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "....r...\n" +
                "........\n" +
                "R...W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_whenPawnAttacksRookDestinationSquare() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "....p...\n" +
                "R...W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        assertE(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "....p...\n" +
                "R...W..R\n");
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
    public void shouldBeAbleToMakeCastling_ifThereIsNoPieceBetweenKingAndRook() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R.B.W..R\n");

        // when
        move(WHITE, from(4, 0).to(7, 0));

        // then
        assertE(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R.B..RW.\n");
        neverFired(WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastlingWithRook_evenIfAnotherRookAlreadyMoved() {

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

        // when trying to make castling with rook that not moved yet
        move(WHITE, from(4, 0).to(7, 0));

        // then
        assertE("........\n" +
                ".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "R.......\n" +
                ".....RW.\n");

        neverFired(WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastling_ifRookIsUnderAttack() {

        givenFl(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "r.......\n" +
                "........\n" +
                "........\n" +
                "R...W..R\n");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        assertE(".w......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "r.......\n" +
                "........\n" +
                "........\n" +
                "..WR...R\n");
        neverFired(WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_withDeadPiece() {

        givenFl("r...w..r\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "....W..R\n");
        move(WHITE, from(7, 0).to(7, 7));
        move(BLACK, from(0, 7).to(0, 6));
        move(WHITE, from(7, 7).to(7, 0));

        // when
        move(BLACK, from(4, 7).to(7, 7));

        // then
        assertE("....w...\n" +
                "r.......\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "....W..R\n");
        fired(BLACK, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifThereIsNoSquaresBetweenKingAndRook() {

        givenFl("....w...\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "......WR\n");

        // when
        move(WHITE, from(6, 0).to(7, 0));

        // then
        assertE("....w...\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "......WR\n");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifThereIsOnlyOneSquareBetweenKingAndRook() {

        givenFl("....w...\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                ".....W.R\n");

        // when
        move(WHITE, from(6, 0).to(7, 0));

        // then
        assertE("....w...\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                ".....W.R\n");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastling_ifThereIsEnoughSpaceBetweenKingAndRook() {

        givenFl("....w...\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "W......R\n");

        // when
        move(WHITE, from(0, 0).to(7, 0));

        // then
        assertE("....w...\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                ".RW.....\n");
        neverFired(WHITE, WRONG_MOVE);
    }
}

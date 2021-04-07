package com.codenjoy.dojo.chess.model;

import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;

public class CastlingTest extends AbstractGameTest {

    /**************************
     *  CASTLING NOT ALLOWED  *
     **************************/

    @Test
    public void shouldNotBeAbleToMakeCastling_ifKingAlreadyMoved() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R...W..R");
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

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R...W..R");
        move(WHITE, from(4, 0).to(0, 0)); // make a castling
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..WR...R");
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
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R...W..R");

        // when try to make one more castling
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastlingWithRook_ifTheRookAlreadyMoved() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R...W..R");
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

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R.B.W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifThereIsEnemyPieceBetweenKingAndRook() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R.b.W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R.b.W..R");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifKingIsUnderACheck() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..b....." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeLongCastling_ifKingShouldPassThroughSquareWhichAttackedByEnemy() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "...r...." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeShortCastling_ifKingShouldPassThroughSquareWhichAttackedByEnemy() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                ".....r.." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(7, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeLongCastling_ifAfterThatKingWillBeUnderCheck() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..r....." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeShortCastling_ifAfterThatKingWillBeUnderCheck() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "......r." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(7, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifRookAndKingIsUnderAttackOfOnePiece() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "q......." +
                "........" +
                "........" +
                "........" +
                "R...W..R");

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

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R.B.W..R");

        // when
        move(WHITE, from(4, 0).to(7, 0));

        // then
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R.B..RW.");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastlingWithRook_evenIfAnotherRookAlreadyMoved() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R...W..R");
        move(WHITE, from(0, 0).to(0, 1));
        move(BLACK, from(1, 7).to(1, 6));

        // when trying to make castling with rook that already moved
        move(WHITE, from(4, 0).to(7, 0));

        // then
        assertE("........" +
                ".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R......." +
                ".....RW.");

        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastling_ifRookIsUnderAttack() {

        // given
        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "r......." +
                "........" +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "r......." +
                "........" +
                "........" +
                "..WR...R");
        neverFired(WHITE, WRONG_MOVE);
    }
}

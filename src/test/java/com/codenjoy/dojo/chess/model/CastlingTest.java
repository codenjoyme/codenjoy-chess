package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.service.Event;
import org.junit.Test;

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
        twoPlayers();
        move(player1, Move.decode(4, 0).to(4, 1));
        move(player2, Move.decode(1, 7).to(1, 6));
        move(player1, Move.decode(4, 1).to(4, 0)); // King returns to it's default place
        move(player2, Move.decode(1, 6).to(1, 7));

        // when try to make castling
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();
        move(player1, Move.decode(4, 0).to(0, 0));

        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..WR...R");

        move(player2, Move.decode(1, 7).to(1, 6));
        move(player1, Move.decode(2, 0).to(2, 1));
        move(player2, Move.decode(1, 6).to(1, 7));
        move(player1, Move.decode(2, 1).to(3, 1));
        move(player2, Move.decode(1, 7).to(1, 6));
        move(player1, Move.decode(3, 1).to(4, 1));
        move(player2, Move.decode(1, 6).to(1, 7));
        move(player1, Move.decode(4, 1).to(4, 0)); // King returns on it's default place
        move(player2, Move.decode(1, 7).to(1, 6));
        move(player1, Move.decode(3, 0).to(0, 0)); // Rook returns on it's default place
        move(player2, Move.decode(1, 6).to(1, 7));

        // when try to make one more castling
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();
        move(player1, Move.decode(0, 0).to(0, 1));
        move(player2, Move.decode(1, 7).to(1, 6));
        move(player1, Move.decode(0, 1).to(0, 0)); // Rook returns on it's default position
        move(player2, Move.decode(1, 6).to(1, 7));

        // when trying to make castling with rook that already moved
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(7, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(7, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        fired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(7, 0));

        // then
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R.B..RW.");
        neverFired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();
        move(player1, Move.decode(0, 0).to(0, 1));
        move(player2, Move.decode(1, 7).to(1, 6));

        // when trying to make castling with rook that already moved
        move(player1, Move.decode(4, 0).to(7, 0));

        // then
        assertE("........" +
                ".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R....RW.");

        neverFired(listener1, Event.WRONG_MOVE);
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
        twoPlayers();

        // when
        move(player1, Move.decode(4, 0).to(0, 0));

        // then
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "r......." +
                "........" +
                "........" +
                "..WR...R");
        neverFired(listener1, Event.WRONG_MOVE);
    }
}

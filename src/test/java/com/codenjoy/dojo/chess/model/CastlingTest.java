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

        player1.getHero().act(Move.from(4, 0).to(4, 1).command());
        game.tick();

        player2.getHero().act(Move.from(1, 7).to(1, 6).command());
        game.tick();

        player1.getHero().act(Move.from(4, 1).to(4, 0).command());
        game.tick(); // King returns to it's default place

        player2.getHero().act(Move.from(1, 6).to(1, 7).command());
        game.tick();

        // when try to make castling
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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

        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..WR...R");

        player2.getHero().act(Move.from(1, 7).to(1, 6).command());
        game.tick();

        player1.getHero().act(Move.from(2, 0).to(2, 1).command());
        game.tick();

        player2.getHero().act(Move.from(1, 6).to(1, 7).command());
        game.tick();

        player1.getHero().act(Move.from(2, 1).to(3, 1).command());
        game.tick();

        player2.getHero().act(Move.from(1, 7).to(1, 6).command());
        game.tick();

        player1.getHero().act(Move.from(3, 1).to(4, 1).command());
        game.tick();

        player2.getHero().act(Move.from(1, 6).to(1, 7).command());
        game.tick();

        player1.getHero().act(Move.from(4, 1).to(4, 0).command());
        game.tick(); // King returns on it's default place

        player2.getHero().act(Move.from(1, 7).to(1, 6).command());
        game.tick();

        player1.getHero().act(Move.from(3, 0).to(0, 0).command());
        game.tick(); // Rook returns on it's default place

        player2.getHero().act(Move.from(1, 6).to(1, 7).command());
        game.tick();

        // when try to make one more castling
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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

        player1.getHero().act(Move.from(0, 0).to(0, 1).command());
        game.tick();

        player2.getHero().act(Move.from(1, 7).to(1, 6).command());
        game.tick();

        player1.getHero().act(Move.from(0, 1).to(0, 0).command());
        game.tick(); // Rook returns on it's default position

        player2.getHero().act(Move.from(1, 6).to(1, 7).command());
        game.tick();

        // when trying to make castling with rook that already moved
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(7, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(7, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(7, 0).command());
        game.tick();

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

        player1.getHero().act(Move.from(0, 0).to(0, 1).command());
        game.tick();

        player2.getHero().act(Move.from(1, 7).to(1, 6).command());
        game.tick();

        // when trying to make castling with rook that already moved
        player1.getHero().act(Move.from(4, 0).to(7, 0).command());
        game.tick();

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
        player1.getHero().act(Move.from(4, 0).to(0, 0).command());
        game.tick();

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

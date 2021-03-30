package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.PieceType;
import com.codenjoy.dojo.chess.service.Event;
import org.junit.Test;

public class PromotionTest extends AbstractGameTest {

    @Test
    public void ShouldBeFiredWrongMove_WhenTryingToPromoteKing() {

        // given
        givenFl("w......." +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        twoPlayers();

        // when
        move(player1, Move.from(7, 6).to(7, 7).promotion(PieceType.KING));

        // then
        assertE("w......." +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        fired(listener1, Event.WRONG_MOVE);
    }

    @Test
    public void ShouldNotBeAllowed_WhenTryingToPromoteIfPawnNotAtLastLine() {

        // given
        givenFl("w......." +
                "........" +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        twoPlayers();

        // when
        move(player1, Move.from(7, 5).to(7, 6).promotion(PieceType.QUEEN));

        // then
        assertE("w......." +
                "........" +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        fired(listener1, Event.WRONG_MOVE);
    }

    @Test
    public void ShouldNotBeAllowed_WhenTryingToMakePromotionByNotAPawn() {

        // given
        givenFl("w......." +
                ".......Q" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKB.WBKR");
        twoPlayers();

        // when
        move(player1, Move.from(7, 6).to(7, 5).promotion(PieceType.PAWN));

        // then
        assertE("w......." +
                ".......Q" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        fired(listener1, Event.WRONG_MOVE);
    }

    @Test
    public void ShouldBeAllowed_WhenTryingToPromoteAPieceExceptKing() {

        // given
        givenFl("w......." +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        twoPlayers();

        // when
        move(player1, Move.from(7, 6).to(7, 7).promotion(PieceType.QUEEN));

        // then
        assertE("w......Q" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        neverFired(listener1, Event.WRONG_MOVE);
    }
}
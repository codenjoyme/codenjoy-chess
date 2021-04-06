package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.PieceType;
import com.codenjoy.dojo.chess.service.Event;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.*;
import static com.codenjoy.dojo.chess.model.Move.*;
import static com.codenjoy.dojo.chess.service.Event.*;

// Wiki: https://en.wikipedia.org/wiki/Promotion_(chess)
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

        // when
        move(WHITE, from(7, 6).to(7, 7).promotion(PieceType.KING));

        // then
        assertE("w......." +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        fired(WHITE, WRONG_MOVE);
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

        // when
        move(WHITE, from(7, 5).to(7, 6).promotion(PieceType.QUEEN));

        // then
        assertE("w......." +
                "........" +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        fired(WHITE, WRONG_MOVE);
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

        // when
        move(WHITE, from(7, 6).to(7, 5).promotion(PieceType.PAWN));

        // then
        assertE("w......." +
                ".......Q" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        fired(WHITE, WRONG_MOVE);
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

        // when
        move(WHITE, from(7, 6).to(7, 7).promotion(PieceType.QUEEN));

        // then
        assertE("w......Q" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        neverFired(WHITE, WRONG_MOVE);
    }
}
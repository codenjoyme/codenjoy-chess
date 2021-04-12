package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.Piece;
import org.junit.Ignore;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.*;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;

@Ignore
public class EnPassant4x4Test extends AbstractGameTest {

    @Test
    public void shouldBeAllowed_whenBlueAttacksRed() {

        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "Y..z...y" +
                ".Z......" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        Piece redPawn = getPieceAt(1, 3);
        move(WHITE, from(4, 1).to(4, 2));
        move(BLACK, from(0, 6).to(0, 5));
        move(RED, from(1, 3).to(2, 3));

        // when
        move(BLUE, from(3, 4).to(2, 3));

        // then
        assertE("rkbqwbkr" +
                ".ppppppp" +
                "p......." +
                "Y......y" +
                "..z....." +
                "....P..." +
                "PPPP.PPP" +
                "RKBQWBKR");
        neverFired(BLUE, WRONG_MOVE);
        assertFalse(redPawn.isAlive());
    }

    @Test
    public void shouldBeAllowed_whenRedAttacksBlue() {

        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "Y.....zy" +
                "...Z...." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        Piece bluePawn = getPieceAt(6, 4);
        move(WHITE, from(4, 1).to(4, 2));
        move(BLACK, from(0, 6).to(0, 5));
        move(RED, from(3, 3).to(4, 3));
        move(BLUE, from(6, 4).to(4, 4));
        move(WHITE, from(0, 1).to(0, 2));
        move(BLACK, from(7, 6).to(7, 5));

        // when
        move(RED, from(4, 3).to(5, 4));

        // then
        assertE("rkbqwbkr" +
                ".pppppp." +
                "p......p" +
                "Y....Z.y" +
                "........" +
                "P...P..." +
                ".PPP.PPP" +
                "RKBQWBKR");
        neverFired(BLUE, WRONG_MOVE);
        assertFalse(bluePawn.isAlive());
    }

    @Test
    public void shouldNotBeAllowedFromSide() {
        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "Y......y" +
                "...Z...." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        move(WHITE, from(4, 1).to(4, 3));
        move(BLACK, from(0, 6).to(0, 5));

        // when trying to do en passant from side
        move(RED, from(3, 3).to(4, 2));

        // then
        assertE("rkbqwbkr" +
                ".ppppppp" +
                "p......." +
                "Y......y" +
                "...ZP..." +
                "........" +
                "PPPP.PPP" +
                "RKBQWBKR");
        fired(RED, WRONG_MOVE);
    }
}

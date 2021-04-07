package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.AbstractGameTest;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;

public class BishopTest extends AbstractGameTest {

    @Test
    public void shouldBeAbleToWalkDiagonal_UpRight() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "B......W");

        // when
        move(WHITE, from(0, 0).to(7, 7));

        // then
        assertE("w......B" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToWalkDiagonal_UpLeft() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "......BW");

        // when
        move(WHITE, from(6, 0).to(0, 6));

        // then
        assertE("w......." +
                "B......." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToWalkDiagonal_DownRight() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "........" +
                "B......." +
                "........" +
                ".......W");

        // when
        move(WHITE, from(0, 2).to(2, 0));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..B....W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToWalkDiagonal_DownLeft() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                ".....B.." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(5, 3).to(2, 0));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..B....W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToTakeEnemyPiece() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                "q......W");
        Piece enemiesQueen = getGameSet(BLACK).getPieceAt(0, 0).get();

        // when
        move(WHITE, from(3, 3).to(0, 0));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "B......W");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(enemiesQueen.isAlive());
    }

    @Test
    public void shouldNotBeAbleToWalkHorizontally_Right() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(4, 3));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkHorizontally_Left() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(1, 3));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkVertically_Up() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(3, 6));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkVertically_Down() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(3, 0));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkThroughEnemyPiece() {

        givenFl("w......." +
                "........" +
                ".....q.." +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(7, 7));

        // then
        assertE("w......." +
                "........" +
                ".....q.." +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkThroughFriendlyPiece() {

        givenFl("w......." +
                "........" +
                ".....Q.." +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(7, 7));

        // then
        assertE("w......." +
                "........" +
                ".....Q.." +
                "........" +
                "...B...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }
}

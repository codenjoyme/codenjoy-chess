package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.AbstractGameTest;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KnightTest extends AbstractGameTest {

    private void knightSurroundedByEnemyPawns() {
        givenFl("w......." +
                "........" +
                "........" +
                "..ppp..." +
                "..pKp..." +
                "..ppp..." +
                "........" +
                ".......W");
    }

    @Test
    public void shouldBeAbleToTakeEnemyPiece() {

        givenFl("w......." +
                "........" +
                "....q..." +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        Piece enemyQueen = getGameSet(BLACK).getPieceAt(4, 5).get();

        // when
        move(WHITE, from(3, 3).to(4, 5));

        // then
        assertE("w......." +
                "........" +
                "....K..." +
                "........" +
                "........" +
                "........" +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(enemyQueen.isAlive());
    }

    @Test
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        givenFl("w......." +
                "........" +
                "....Q..." +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        Piece friendlyQueen = getGameSet(WHITE).getPieceAt(4, 5).get();

        // when
        move(WHITE, from(3, 3).to(4, 5));

        // then
        assertE("w......." +
                "........" +
                "....Q..." +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
        assertTrue(friendlyQueen.isAlive());
    }

    @Test
    public void shouldBeAbleToJumpOverPieces_LeftDown() {

        // given
        knightSurroundedByEnemyPawns();

        // when
        move(WHITE, from(3, 3).to(1, 2));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "..ppp..." +
                "..p.p..." +
                ".Kppp..." +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToJumpOverPieces_LeftUp() {

        // given
        knightSurroundedByEnemyPawns();

        // when
        move(WHITE, from(3, 3).to(1, 4));

        // then
        assertE("w......." +
                "........" +
                "........" +
                ".Kppp..." +
                "..p.p..." +
                "..ppp..." +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToJumpOverPieces_RightDown() {

        // given
        knightSurroundedByEnemyPawns();

        // when
        move(WHITE, from(3, 3).to(5, 2));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "..ppp..." +
                "..p.p..." +
                "..pppK.." +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToJumpOverPieces_RightUp() {

        // given
        knightSurroundedByEnemyPawns();

        // when
        move(WHITE, from(3, 3).to(5, 4));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "..pppK.." +
                "..p.p..." +
                "..ppp..." +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToJumpOverPieces_UpLeft() {

        // given
        knightSurroundedByEnemyPawns();

        // when
        move(WHITE, from(3, 3).to(2, 5));

        // then
        assertE("w......." +
                "........" +
                "..K....." +
                "..ppp..." +
                "..p.p..." +
                "..ppp..." +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToJumpOverPieces_UpRight() {

        // given
        knightSurroundedByEnemyPawns();

        // when
        move(WHITE, from(3, 3).to(4, 5));

        // then
        assertE("w......." +
                "........" +
                "....K..." +
                "..ppp..." +
                "..p.p..." +
                "..ppp..." +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToJumpOverPieces_DownLeft() {

        // given
        knightSurroundedByEnemyPawns();

        // when
        move(WHITE, from(3, 3).to(2, 1));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "..ppp..." +
                "..p.p..." +
                "..ppp..." +
                "..K....." +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToJumpOverPieces_DownRight() {

        // given
        knightSurroundedByEnemyPawns();

        // when
        move(WHITE, from(3, 3).to(4, 1));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "..ppp..." +
                "..p.p..." +
                "..ppp..." +
                "....K..." +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkHorizontally_Right() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
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
                "...K...." +
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
                "...K...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(2, 3));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
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
                "...K...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(3, 5));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
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
                "...K...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(3, 1));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkDiagonally_UpRight() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(4, 4));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkDiagonally_UpLeft() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(2, 4));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkDiagonally_DownRight() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(4, 2));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToWalkDiagonally_DownLeft() {

        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");

        // when
        move(WHITE, from(3, 3).to(1, 1));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
    }
}

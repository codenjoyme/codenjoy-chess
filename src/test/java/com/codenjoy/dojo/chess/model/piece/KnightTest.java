package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.AbstractGameTest;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.service.Event;
import org.junit.Test;

public class KnightTest extends AbstractGameTest {

    private void knightSurroundedByEnemyPawnsAndTwoPlayers() {
        givenFl("w......." +
                "........" +
                "........" +
                "..ppp..." +
                "..pKp..." +
                "..ppp..." +
                "........" +
                ".......W");
        twoPlayers();
    }

    @Test
    public void shouldBeAbleToMove_inAccordanceWithChessRules() {

        // given
        knightSurroundedByEnemyPawnsAndTwoPlayers();
        // when go left-down
        move(whitePlayer, Move.from(3, 3).to(1, 2));

        // given
        knightSurroundedByEnemyPawnsAndTwoPlayers();
        // when go left-up
        move(whitePlayer, Move.from(3, 3).to(1, 4));

        // given
        knightSurroundedByEnemyPawnsAndTwoPlayers();
        // when go right-down
        move(whitePlayer, Move.from(3, 3).to(5, 2));

        // given
        knightSurroundedByEnemyPawnsAndTwoPlayers();
        // when go right-up
        move(whitePlayer, Move.from(3, 3).to(5, 4));

        // given
        knightSurroundedByEnemyPawnsAndTwoPlayers();
        // when go up-left
        move(whitePlayer, Move.from(3, 3).to(2, 5));

        // given
        knightSurroundedByEnemyPawnsAndTwoPlayers();
        // when go up-right
        move(whitePlayer, Move.from(3, 3).to(4, 5));

        // given
        knightSurroundedByEnemyPawnsAndTwoPlayers();
        // when go down-left
        move(whitePlayer, Move.from(3, 3).to(2, 1));

        // given
        knightSurroundedByEnemyPawnsAndTwoPlayers();
        // when go down-right
        move(whitePlayer, Move.from(3, 3).to(4, 1));

        // then
        neverFired(whiteListener, Event.WRONG_MOVE);
    }
}

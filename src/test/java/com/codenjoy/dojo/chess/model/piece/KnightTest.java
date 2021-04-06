package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.AbstractGameTest;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.service.Event.WRONG_MOVE;

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
    public void shouldBeAbleToMove_inAccordanceWithChessRules() {

        // given
        knightSurroundedByEnemyPawns();
        // when go left-down
        move(WHITE, from(3, 3).to(1, 2));

        // given
        knightSurroundedByEnemyPawns();
        // when go left-up
        move(WHITE, from(3, 3).to(1, 4));

        // given
        knightSurroundedByEnemyPawns();
        // when go right-down
        move(WHITE, from(3, 3).to(5, 2));

        // given
        knightSurroundedByEnemyPawns();
        // when go right-up
        move(WHITE, from(3, 3).to(5, 4));

        // given
        knightSurroundedByEnemyPawns();
        // when go up-left
        move(WHITE, from(3, 3).to(2, 5));

        // given
        knightSurroundedByEnemyPawns();
        // when go up-right
        move(WHITE, from(3, 3).to(4, 5));

        // given
        knightSurroundedByEnemyPawns();
        // when go down-left
        move(WHITE, from(3, 3).to(2, 1));

        // given
        knightSurroundedByEnemyPawns();
        // when go down-right
        move(WHITE, from(3, 3).to(4, 1));

        // then
        neverFired(WHITE, WRONG_MOVE);
    }
}

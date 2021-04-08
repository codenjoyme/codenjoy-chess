package com.codenjoy.dojo.chess.model;

import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.*;

public class FourPlayersGameTest extends AbstractGameTest {

    private String fourPlayersBoard() {
        return  "  rkbqwbkr  " +
                "  pppppppp  " +
                "IZ........zi" +
                "LZ........zl" +
                "GZ........zg" +
                "XZ........zx" +
                "YZ........zy" +
                "GZ........zg" +
                "LZ........zl" +
                "IZ........zi" +
                "  PPPPPPPP  " +
                "  RKBQWBKR  ";
    }

    @Test
    public void shouldDrawBoardProperlyForWhites() {

        // when
        givenFl(fourPlayersBoard());

        // then
        assertE(fourPlayersBoard(), WHITE);
    }

    @Test
    public void shouldDrawBoardProperlyForBlacks() {

        // when
        givenFl(fourPlayersBoard());

        // then
        assertE(fourPlayersBoard(), BLACK);
    }

    @Test
    public void shouldDrawBoardProperlyForReds() {

        // when
        givenFl(fourPlayersBoard());

        // then
        assertE(fourPlayersBoard(), RED);
    }

    @Test
    public void shouldDrawBoardProperlyForBlues() {

        // when
        givenFl(fourPlayersBoard());

        // then
        assertE(fourPlayersBoard(), BLUE);
    }
}

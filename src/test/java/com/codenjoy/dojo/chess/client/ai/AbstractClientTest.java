package com.codenjoy.dojo.chess.client.ai;

import com.codenjoy.dojo.chess.client.Board;
import com.codenjoy.dojo.utils.LevelUtils;

public class AbstractClientTest {

    protected Board board;

    protected void given(String board) {
        String cleanBoard = LevelUtils.clear(board);
        this.board = (Board) new Board().forString(cleanBoard);
    }

    @SuppressWarnings("SpellCheckingInspection")
    protected String classicFFABoard() {
        return  "   rkbwqbkr   \n" +
                "   pppppppp   \n" +
                "   ........   \n" +
                "IZ..........zi\n" +
                "LZ..........zl\n" +
                "GZ..........zg\n" +
                "YZ..........zx\n" +
                "XZ..........zy\n" +
                "GZ..........zg\n" +
                "LZ..........zl\n" +
                "IZ..........zi\n" +
                "   ........   \n" +
                "   PPPPPPPP   \n" +
                "   RKBQWBKR   \n";
    }

    @SuppressWarnings("SpellCheckingInspection")
    protected String classicBoard() {
        return  "rkbqwbkr\n" +
                "pppppppp\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n";
    }

}

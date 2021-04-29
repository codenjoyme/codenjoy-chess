package com.codenjoy.dojo.chess.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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

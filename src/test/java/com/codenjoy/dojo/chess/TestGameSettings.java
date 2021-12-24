package com.codenjoy.dojo.chess;

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

import com.codenjoy.dojo.chess.model.level.Levels;
import com.codenjoy.dojo.chess.service.GameSettings;

import static com.codenjoy.dojo.chess.service.GameSettings.Option.*;

public class TestGameSettings extends GameSettings {

    /**
     * Here you can override the settings for all tests.
     */
    public TestGameSettings() {
        bool(GAME_OVER_IF_WRONG_MOVE, false);
        bool(WAIT_UNTIL_MAKE_A_MOVE, true);
        bool(LAST_PLAYER_STAYS, false);

        integer(WIN_SCORE, 100);
        integer(WRONG_MOVE_PENALTY, 0);
        integer(GAME_OVER_PENALTY, -10);

        integer(KING_WORTH, 20);
        integer(QUEEN_WORTH, 9);
        integer(BISHOP_WORTH, 5);
        integer(ROOK_WORTH, 5);
        integer(KNIGHT_WORTH, 3);
        integer(PAWN_WORTH, 1);

        multiline(LEVEL_MAP, Levels.classicFourPlayerChessBoard());
    }
}

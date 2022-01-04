package com.codenjoy.dojo.chess.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.client.Utils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameSettingsTest {

    @Test
    public void shouldGetAllKeys() {
        assertEquals("[LEVEL_MAP, \n" +
                        "GAME_OVER_IF_WRONG_MOVE, \n" +
                        "WAIT_UNTIL_MAKE_A_MOVE, \n" +
                        "LAST_PLAYER_STAYS, \n" +
                        "WIN_SCORE, \n" +
                        "WRONG_MOVE_PENALTY, \n" +
                        "GAME_OVER_PENALTY, \n" +
                        "KING_WORTH, \n" +
                        "QUEEN_WORTH, \n" +
                        "BISHOP_WORTH, \n" +
                        "ROOK_WORTH, \n" +
                        "KNIGHT_WORTH, \n" +
                        "PAWN_WORTH]",
                Utils.split(new GameSettings().allKeys(), ", \n"));
    }

}

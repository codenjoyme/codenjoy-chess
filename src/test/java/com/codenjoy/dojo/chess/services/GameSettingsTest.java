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
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameSettingsTest {

    @Test
    public void shouldGetAllKeys() {
        assertEquals("LEVEL_MAP               =[Level] Level map\n" +
                    "GAME_OVER_IF_WRONG_MOVE =[Game] Game over if tried to make wrong move\n" +
                    "WAIT_UNTIL_MAKE_A_MOVE  =[Game] Wait a player's move if not responds in time\n" +
                    "LAST_PLAYER_STAYS       =[Game] Last alive player continues to play\n" +
                    "WIN_SCORE               =[Score] Victory reward\n" +
                    "WRONG_MOVE_PENALTY      =[Score] Wrong move penalty\n" +
                    "GAME_OVER_PENALTY       =[Score] Game over penalty\n" +
                    "KING_WORTH              =[Score] King piece worth\n" +
                    "QUEEN_WORTH             =[Score] Queen piece worth\n" +
                    "BISHOP_WORTH            =[Score] Bishop piece worth\n" +
                    "ROOK_WORTH              =[Score] Rook piece worth\n" +
                    "KNIGHT_WORTH            =[Score] Knight piece worth\n" +
                    "PAWN_WORTH              =[Score] Pawn piece worth",
                TestUtils.toString(new GameSettings().allKeys()));
    }
}
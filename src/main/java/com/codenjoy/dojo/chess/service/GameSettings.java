package com.codenjoy.dojo.chess.service;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.chess.model.level.Levels;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.google.common.collect.Lists;

import java.util.List;

import static com.codenjoy.dojo.chess.service.GameSettings.Option.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    @Override
    public List<Key> allKeys() {
        return Lists.newArrayList(Option.values());
    }

    public enum Option implements Key {

        LEVEL_MAP("Level map"),
        GAME_OVER_IF_WRONG_MOVE("Game over if tried to make wrong move"),
        WAIT_UNTIL_MAKE_A_MOVE("Wait a player's move if not responds in time"),
        VICTORY_REWARD("Victory reward"),
        WRONG_MOVE_PENALTY("Wrong move penalty"),
        GAME_OVER_PENALTY("Game over penalty"),
        LAST_PLAYER_STAYS("Last alive player continues to play"),

        KING_WORTH("The worth of king piece"),
        QUEEN_WORTH("The worth of queen piece"),
        BISHOP_WORTH("The worth of bishop piece"),
        ROOK_WORTH("The worth of rook piece"),
        KNIGHT_WORTH("The worth of knight piece"),
        PAWN_WORTH("The worth of pawn piece");

        private final String key;

        Option(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    public GameSettings() {
        bool(GAME_OVER_IF_WRONG_MOVE, false);
        bool(WAIT_UNTIL_MAKE_A_MOVE, true);
        bool(LAST_PLAYER_STAYS, true);

        integer(VICTORY_REWARD, 100);
        integer(WRONG_MOVE_PENALTY, 0);
        integer(GAME_OVER_PENALTY, 10);

        integer(KING_WORTH, 20);
        integer(QUEEN_WORTH, 9);
        integer(BISHOP_WORTH, 5);
        integer(ROOK_WORTH, 5);
        integer(KNIGHT_WORTH, 3);
        integer(PAWN_WORTH, 1);

        multiline(LEVEL_MAP, Levels.classicChessBoard());
    }

    public Level level() {
        return new Level(string(LEVEL_MAP));
    }
}
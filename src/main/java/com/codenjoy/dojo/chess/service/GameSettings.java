package com.codenjoy.dojo.chess.service;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
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


import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.chess.model.level.Levels;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.settings.PropertiesKey;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.google.common.collect.Lists;

import java.util.List;

import static com.codenjoy.dojo.chess.service.GameRunner.GAME_NAME;
import static com.codenjoy.dojo.chess.service.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    @Override
    public List<Key> allKeys() {
        return Lists.newArrayList(Keys.values());
    }

    public enum Keys implements PropertiesKey {

        LEVEL_MAP,
        GAME_OVER_IF_WRONG_MOVE,
        WAIT_UNTIL_MAKE_A_MOVE,
        LAST_PLAYER_STAYS,
        WIN_SCORE,
        WRONG_MOVE_PENALTY,
        GAME_OVER_PENALTY,
        KING_WORTH,
        QUEEN_WORTH,
        BISHOP_WORTH,
        ROOK_WORTH,
        KNIGHT_WORTH,
        PAWN_WORTH;

        private final String key;

        Keys() {
            this.key = key(GAME_NAME);
        }

        @Override
        public String key() {
            return key;
        }
    }

    public GameSettings() {
        bool(GAME_OVER_IF_WRONG_MOVE, true);
        bool(WAIT_UNTIL_MAKE_A_MOVE, false);
        bool(LAST_PLAYER_STAYS, true);

        integer(WIN_SCORE, 100);
        integer(WRONG_MOVE_PENALTY, -1);
        integer(GAME_OVER_PENALTY, -10);

        integer(KING_WORTH, 20);
        integer(QUEEN_WORTH, 9);
        integer(BISHOP_WORTH, 5);
        integer(ROOK_WORTH, 5);
        integer(KNIGHT_WORTH, 3);
        integer(PAWN_WORTH, 1);

        multiline(LEVEL_MAP, Levels.classicFourPlayerChessBoard());
    }

    public Level level() {
        return new Level(string(LEVEL_MAP));
    }

    public boolean gameOverIfWrongMove() {
        return bool(GAME_OVER_IF_WRONG_MOVE);
    }

    public boolean waitUntilMakeAMove() {
        return bool(WAIT_UNTIL_MAKE_A_MOVE);
    }

    public int victoryScore() {
        return integer(WIN_SCORE);
    }

    public int wrongMovePenalty() {
        return integer(WRONG_MOVE_PENALTY);
    }

    public int gameOverPenalty() {
        return integer(GAME_OVER_PENALTY);
    }

    public boolean lastPlayerStays() {
        return bool(LAST_PLAYER_STAYS);
    }

    public int worthOf(Piece.Type type) {
        switch (type) {
            case KING:
                return integer(KING_WORTH);
            case QUEEN:
                return integer(QUEEN_WORTH);
            case KNIGHT:
                return integer(KNIGHT_WORTH);
            case BISHOP:
                return integer(BISHOP_WORTH);
            case ROOK:
                return integer(ROOK_WORTH);
            case PAWN:
                return integer(PAWN_WORTH);
            default:
                throw new IllegalArgumentException("Piece type " + type + " not supported");
        }
    }

    public Calculator<Void> calculator() {
        return new Calculator<>(new Scores(this));
    }
}
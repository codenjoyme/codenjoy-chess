package com.codenjoy.dojo.chess.service;

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


import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.chess.service.GameSettings.Keys.*;

public class Scores extends ScoresMap<Void> {

    public Scores(SettingsReader settings) {
        super(settings);

        put(Event.WIN,
                value -> settings.integer(WIN_SCORE));

        put(Event.WRONG_MOVE,
                value -> settings.integer(WRONG_MOVE_PENALTY));

        put(Event.GAME_OVER,
                value -> settings.integer(GAME_OVER_PENALTY));

        put(Event.KING_TAKEN,
                value -> settings.integer(KING_WORTH));

        put(Event.QUEEN_TAKEN,
                value -> settings.integer(QUEEN_WORTH));

        put(Event.BISHOP_TAKEN,
                value -> settings.integer(BISHOP_WORTH));

        put(Event.ROOK_TAKEN,
                value -> settings.integer(ROOK_WORTH));

        put(Event.KNIGHT_TAKEN,
                value -> settings.integer(KNIGHT_WORTH));

        put(Event.PAWN_TAKEN,
                value -> settings.integer(PAWN_WORTH));
    }
}
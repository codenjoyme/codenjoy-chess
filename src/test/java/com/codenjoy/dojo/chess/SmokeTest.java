package com.codenjoy.dojo.chess;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.games.chess.Board;
import com.codenjoy.dojo.chess.service.ai.AISolver;
import com.codenjoy.dojo.chess.model.level.Levels;
import com.codenjoy.dojo.chess.service.GameRunner;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Test;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.codenjoy.dojo.chess.service.GameSettings.Option.*;
import static java.util.stream.Collectors.toList;

public class SmokeTest {

    @Test
    public void testClassicChessBoard() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 200);

        // about 1.5 sec
        int players = 2;
        int ticks = 1000;

        Supplier<Solver> solver = () -> new AISolver(dice);

        LocalGameRunner.removeWhenWin = true;
        LocalGameRunner.removeWhenGameOver = true;
        LocalGameRunner.reloadPlayersWhenGameOverAll = true;

        Smoke.play(ticks, "SmokeTestClassicChessBoard.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return super.getSettings()
                                .integer(WRONG_MOVE_PENALTY, 1)
                                .integer(GAME_OVER_PENALTY, 1)
                                .string(LEVEL_MAP, Levels.classicChessBoard());
                    }
                },
                Stream.generate(solver)
                        .limit(players).collect(toList()),
                Stream.generate(() -> new Board())
                        .limit(players).collect(toList()));
    }

    @Test
    public void testClassicFourPlayerChessBoard() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 200);

        // about 9 sec
        int players = 4;
        int ticks = 1000;

        Supplier<Solver> solver = () -> new AISolver(dice);

        LocalGameRunner.removeWhenWin = true;
        LocalGameRunner.removeWhenGameOver = true;
        LocalGameRunner.reloadPlayersWhenGameOverAll = true;

        Smoke.play(ticks, "SmokeTestClassicFourPlayerChessBoard.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return super.getSettings()
                                .integer(WRONG_MOVE_PENALTY, 1)
                                .integer(GAME_OVER_PENALTY, 1)
                                .string(LEVEL_MAP, Levels.classicFourPlayerChessBoard());
                    }
                },
                Stream.generate(solver)
                        .limit(players).collect(toList()),
                Stream.generate(() -> new Board())
                        .limit(players).collect(toList()));
    }
}

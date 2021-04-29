package com.codenjoy.dojo.chess;

import com.codenjoy.dojo.chess.model.level.Levels;
import com.codenjoy.dojo.chess.service.GameSettings;

import static com.codenjoy.dojo.chess.service.GameSettings.Option.*;

public class TestGameSettings extends GameSettings {

    public TestGameSettings() {
        bool(GAME_OVER_IF_WRONG_MOVE, false);
        bool(WAIT_UNTIL_MAKE_A_MOVE, true);
        bool(LAST_PLAYER_STAYS, false);

        integer(VICTORY_REWARD, 100);
        integer(WRONG_MOVE_PENALTY, 0);
        integer(GAME_OVER_PENALTY, 10);

        integer(KING_WORTH, 20);
        integer(QUEEN_WORTH, 9);
        integer(BISHOP_WORTH, 5);
        integer(ROOK_WORTH, 5);
        integer(KNIGHT_WORTH, 3);
        integer(PAWN_WORTH, 1);

        multiline(LEVEL_MAP, Levels.classicFourPlayerChessBoard());
    }
}

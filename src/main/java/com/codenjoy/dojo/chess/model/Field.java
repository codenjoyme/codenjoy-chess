package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.services.multiplayer.GameField;

public interface Field extends GameField<Player> {

    Color getAvailableColor();

    GameBoard getBoard();

    Color getCurrentColor();

    Rotator getRotator();
}

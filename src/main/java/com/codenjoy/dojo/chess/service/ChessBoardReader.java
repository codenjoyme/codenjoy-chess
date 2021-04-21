package com.codenjoy.dojo.chess.service;

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

import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessBoardReader implements BoardReader<Player> {

    private final Chess game;

    public ChessBoardReader(Chess game) {
        this.game = game;
    }

    @Override
    public int size() {
        return game.getBoardSize();
    }

    @Override
    public Iterable<? extends Point> elements(Player player) {
        GameBoard board = game.getBoard();

        List<ReaderElement> pieces = board.getPieces().stream()
                    .filter(Piece::isAlive)
                    .filter(piece -> !game.isPlayerAskedForColor(player) || piece.getColor() == player.getColor())
                    .map(ReaderElement::create)
                    .collect(Collectors.toList());

        game.colorAnswered(player.getColor());

        List<ReaderElement> barriers = board.getBarriers().stream()
                .map(ReaderElement::create)
                .collect(Collectors.toList());


        List<ReaderElement> squares = board.getSquares().stream()
                .map(ReaderElement::create)
                .collect(Collectors.toList());

        ArrayList<ReaderElement> elements = Lists.newArrayList(pieces);
        elements.addAll(barriers);
        elements.addAll(squares);

        // rotate board towards the player by his side
        Rotator rotator = game.getRotator();
        Direction directionFrom = player.getColor().getAttackDirection();
        Direction directionTo = Chess.getDefaultAttackDirection();
        elements.forEach(e -> rotator.mapPosition(e, directionFrom, directionTo));

        return elements;
    }
}

package com.codenjoy.dojo.chess.engine.service;

import com.codenjoy.dojo.chess.engine.model.item.piece.Piece;
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
                .map(ReaderElement::create)
                .collect(Collectors.toList());

        List<ReaderElement> barriers = board.getBarriers().stream()
                .map(ReaderElement::create)
                .collect(Collectors.toList());


        List<ReaderElement> squares = board.getSquares().stream()
                .map(ReaderElement::create)
                .collect(Collectors.toList());

        ArrayList<ReaderElement> elements = Lists.newArrayList(pieces);
        elements.addAll(barriers);
        elements.addAll(squares);

        game.getPositionMapper()
                .mapPosition(player.getColor(), elements);
        return elements;
    }
}

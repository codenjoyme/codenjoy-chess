package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.List;

public class GameSet extends PlayerHero<Board> {

    private final Color color;
    private final List<Piece> pieces;

    public GameSet(Color color, List<Piece> pieces) {
        this.color = color;
        this.pieces = pieces;
    }

    public Color getColor() {
        return color;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    @Override
    public void down() {
        // not supported
    }

    @Override
    public void up() {
        // not supported
    }

    @Override
    public void left() {
        // not supported
    }

    @Override
    public void right() {
        // not supported
    }

    @Override
    public void act(int... p) {

    }

    @Override
    public void tick() {

    }
}

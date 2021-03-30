package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.List;
import java.util.Optional;

public class GameSet extends PlayerHero<Field> {

    private final List<Piece> pieces;

    private Move command;

    public GameSet(List<Piece> pieces) {
        if (pieces.isEmpty()) {
            throw new IllegalArgumentException("Game set should contain at least one piece");
        }
        this.pieces = pieces;
    }

    public Color getColor() {
        return pieces.get(0).getColor();
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public Optional<Piece> getPieceAt(int x, int y) {
        return getPieceAt(new PointImpl(x, y));
    }

    public Optional<Piece> getPieceAt(Point position) {
        return pieces.stream()
                .filter(p -> p.getPosition().equals(position))
                .findFirst();
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
        if (p.length != 4) {
            throw new IllegalArgumentException();
        }
        command = Move.from(p[0], p[1]).to(p[2], p[3]);

    }

    @Override
    public void tick() {
        if (command == null) {
            return;
        }
        Piece piece = field.getAt(command.getFrom())
                .orElse(null);
        piece.getAvailableMoves(field);
        piece.move(command.getTo());
        command = null;
    }
}

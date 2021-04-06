package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.Pawn;
import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.chess.model.piece.PieceType;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameSet extends PlayerHero<Board> {

    private final List<Piece> pieces;
    private Move command;
    private boolean triedWrongMove;
    private Move lastMove = null;

    public GameSet(List<Piece> pieces, Board board) {
        if (pieces.isEmpty()) {
            throw new IllegalArgumentException("Game set should contain at least one piece");
        }
        List<Piece> kings = pieces.stream().filter(p -> p.getType() == PieceType.KING)
                .collect(Collectors.toList());
        if (kings.size() != 1) {
            throw new IllegalArgumentException("Should be exactly 1 king piece in game set");
        }
        this.pieces = pieces;
        init(board);
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

    public Map<Piece, List<Point>> getAvailableMoves() {
        return pieces.stream()
                .collect(Collectors.toMap(Function.identity(), Piece::getAvailableMoves));
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
    public void act(int... codes) {
        command = Move.decode(codes);
    }

    @Override
    public void tick() {
        triedWrongMove = false;
        if (command == null) {
            return;
        }
        Piece piece = field.getAt(command.getFrom())
                .orElse(null);

        if (piece.getAvailableMoves().contains(command.getTo())) {
            field.getAt(command.getTo()).ifPresent(p -> p.setAlive(false));
            piece.move(command.getTo());
            lastMove = command;
        } else {
            triedWrongMove = true;
            lastMove = null;
        }
        command = null;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public boolean isTriedWrongMove() {
        return triedWrongMove;
    }
}

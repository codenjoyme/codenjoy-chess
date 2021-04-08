package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.*;
import com.codenjoy.dojo.services.Direction;
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

    public Map<Piece, List<Move>> getAvailableMoves() {
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
        if (piece.getColor() != getColor()) {
            triedWrongMove = true;
            lastMove = null;
            command = null;
            return;
        }
        if (piece.getAvailableMoves().contains(command)) {
            if (isCastling(command)) {
                // castling
                if (!tryCastling((Rook) field.getAt(command.getTo()).get())) {
                    triedWrongMove = true;
                    lastMove = null;
                } else {
                    lastMove = command;
                }
                command = null;
                return;
            } else {
                piece.move(command);
                lastMove = command;
                if (command.withPromotion()) {
                    pieces.remove(piece);
                    pieces.add(command.getPromotion().getConstructor().apply(getColor(), field, piece.getPosition()));
                }
            }
        } else {
            triedWrongMove = true;
            lastMove = null;
        }
        command = null;
    }

    private boolean isCastling(Move move) {
        Optional<Piece> optPieceOne = field.getAt(command.getFrom());
        Optional<Piece> optPieceTwo = field.getAt(command.getTo());
        if (optPieceOne.isEmpty() || optPieceTwo.isEmpty()) {
            return false;
        }
        Piece pieceOne = optPieceOne.get();
        Piece pieceTwo = optPieceTwo.get();
        if (pieceOne.getColor() != pieceTwo.getColor()) {
            return false;
        }
        return pieceOne.getType() == PieceType.KING && pieceTwo.getType() == PieceType.ROOK;
    }

    private boolean tryCastling(Rook rook) {
        King king = getKing();
        if (king == null) {
            throw new IllegalStateException("King is null");
        }
        if (rook.isMoved() || king.isMoved()) {
            return false;
        }
        if (field.isUnderAttack(king.getPosition(), getColor())) {
            return false;
        }
        Direction direction = defineDirection(king.getPosition(), rook.getPosition());
        if (direction == null) {
            throw new IllegalStateException();
        }
        Point rookPosition = direction.change(king.getPosition());
        if (field.getAt(rookPosition).isPresent() || field.isUnderAttack(rookPosition, getColor())) {
            return false;
        }
        Point kingPosition = direction.change(rookPosition);
        if (field.getAt(kingPosition).isPresent() || field.isUnderAttack(kingPosition, getColor())) {
            return false;
        }
        rook.move(rookPosition);
        king.move(kingPosition);
        return true;
    }

    private Direction defineDirection(Point from, Point to) {
        if (from.equals(to)) {
            return null;
        }
        if (from.getX() == to.getX()) {
            return from.getY() < to.getY() ? Direction.UP : Direction.DOWN;
        }
        if (from.getY() == to.getY()) {
            return from.getX() < to.getX() ? Direction.RIGHT : Direction.LEFT;
        }
        return null;
    }

    private King getKing() {
        return (King) pieces.stream()
                .filter(p -> p.getType() == PieceType.KING)
                .findFirst().orElse(null);
    }

    public Move getLastMove() {
        return lastMove;
    }

    public boolean isTriedWrongMove() {
        return triedWrongMove;
    }
}

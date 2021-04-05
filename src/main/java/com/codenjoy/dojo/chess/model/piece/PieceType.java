package com.codenjoy.dojo.chess.model.piece;

import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.Board;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.TriFunction;


public enum PieceType {
    KING(0, King::new),
    QUEEN(1, Queen::new),
    KNIGHT(2, Knight::new),
    BISHOP(3, Bishop::new),
    ROOK(4, Rook::new),
    PAWN(5, Pawn::new);

    private final int id;
    private final TriFunction<Color, Board, Point, Piece> constructor;

    PieceType(int id, TriFunction<Color, Board, Point, Piece> constructor) {
        this.id = id;
        this.constructor = constructor;
    }

    public int getId() {
        return id;
    }

    public TriFunction<Color, Board, Point, Piece> getConstructor() {
        return constructor;
    }

    public static PieceType byId(int id) {
        for (PieceType type : PieceType.values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}

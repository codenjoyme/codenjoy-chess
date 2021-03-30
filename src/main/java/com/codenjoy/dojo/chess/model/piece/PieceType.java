package com.codenjoy.dojo.chess.model.piece;

public enum PieceType {
    KING(0),
    QUEEN(1),
    KNIGHT(2),
    BISHOP(3),
    ROOK(4),
    PAWN(5);

    private final int id;

    PieceType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.*;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.chess.model.Color.BLACK;
import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.piece.PieceType.*;
import static com.codenjoy.dojo.chess.model.piece.PieceType.PAWN;

public class PiecesFactory {

    public static Piece create(Point position, Element element) {
        switch (element) {
            case WHITE_KING:
                return new King(KING, WHITE, position);
            case BLACK_KING:
                return new King(KING, BLACK, position);

            case WHITE_QUEEN:
                return new Queen(QUEEN, WHITE, position);
            case BLACK_QUEEN:
                return new Queen(QUEEN, BLACK, position);

            case WHITE_KNIGHT:
                return new Knight(KNIGHT, WHITE, position);
            case BLACK_KNIGHT:
                return new Knight(KNIGHT, BLACK, position);

            case WHITE_BISHOP:
                return new Bishop(BISHOP, WHITE, position);
            case BLACK_BISHOP:
                return new Bishop(BISHOP, BLACK, position);

            case WHITE_ROOK:
                return new Rook(ROOK, WHITE, position);
            case BLACK_ROOK:
                return new Rook(ROOK, BLACK, position);

            case WHITE_PAWN:
                return new Pawn(PAWN, WHITE, position);
            case BLACK_PAWN:
                return new Pawn(PAWN, BLACK, position);

            default:
                throw new IllegalArgumentException("Element " + element + " is not a chess piece");
        }
    }
}

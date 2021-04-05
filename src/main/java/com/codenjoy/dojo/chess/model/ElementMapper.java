package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.chess.model.piece.PieceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.codenjoy.dojo.chess.model.Color.*;
import static com.codenjoy.dojo.chess.model.Element.*;
import static com.codenjoy.dojo.chess.model.piece.PieceType.*;

public class ElementMapper {
    private static final Map<ColorAndType, Element> elementsByColorAndType = new HashMap<>() {{
        put(new ColorAndType(WHITE, KING), WHITE_KING);
        put(new ColorAndType(WHITE, QUEEN), WHITE_QUEEN);
        put(new ColorAndType(WHITE, KNIGHT), WHITE_KNIGHT);
        put(new ColorAndType(WHITE, BISHOP), WHITE_BISHOP);
        put(new ColorAndType(WHITE, ROOK), WHITE_ROOK);
        put(new ColorAndType(WHITE, PAWN), WHITE_PAWN);

        put(new ColorAndType(BLACK, KING), BLACK_KING);
        put(new ColorAndType(BLACK, QUEEN), BLACK_QUEEN);
        put(new ColorAndType(BLACK, KNIGHT), BLACK_KNIGHT);
        put(new ColorAndType(BLACK, BISHOP), BLACK_BISHOP);
        put(new ColorAndType(BLACK, ROOK), BLACK_ROOK);
        put(new ColorAndType(BLACK, PAWN), BLACK_PAWN);
    }};

    public static Element mapToElement(Piece piece) {
        return mapToElement(piece.getColor(), piece.getType());
    }

    public static Element mapToElement(Color color, PieceType pieceType) {
        return elementsByColorAndType.get(new ColorAndType(color, pieceType));
    }

    private static class ColorAndType {
        private final Color color;
        private final PieceType type;

        public ColorAndType(Color color, PieceType type) {
            this.color = color;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ColorAndType that = (ColorAndType) o;
            return color == that.color && type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(color, type);
        }
    }
}

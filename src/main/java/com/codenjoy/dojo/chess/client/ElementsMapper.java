package com.codenjoy.dojo.chess.client;

import com.codenjoy.dojo.chess.model.Elements;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementsMapper {
    private static final Map<Color, List<Elements>> colorsToPieces = new HashMap<>();

    static {
        colorsToPieces.put(Color.WHITE, Lists.newArrayList(
                Elements.WHITE_KING,
                Elements.WHITE_QUEEN,
                Elements.WHITE_BISHOP,
                Elements.WHITE_ROOK,
                Elements.WHITE_KNIGHT,
                Elements.WHITE_PAWN
        ));
        colorsToPieces.put(Color.BLACK, Lists.newArrayList(
                Elements.BLACK_KING,
                Elements.BLACK_QUEEN,
                Elements.BLACK_BISHOP,
                Elements.BLACK_ROOK,
                Elements.BLACK_KNIGHT,
                Elements.BLACK_PAWN
        ));
        colorsToPieces.put(Color.RED, Lists.newArrayList(
                Elements.RED_KING,
                Elements.RED_QUEEN,
                Elements.RED_BISHOP,
                Elements.RED_ROOK,
                Elements.RED_KNIGHT,
                Elements.RED_PAWN
        ));
        colorsToPieces.put(Color.BLUE, Lists.newArrayList(
                Elements.BLUE_KING,
                Elements.BLUE_QUEEN,
                Elements.BLUE_BISHOP,
                Elements.BLUE_ROOK,
                Elements.BLUE_KNIGHT,
                Elements.BLUE_PAWN
        ));
    }

    public static List<Elements> getElements(Color color) {
        return colorsToPieces.get(color);
    }

    public static Color getColor(Elements piece) {
        return colorsToPieces.entrySet().stream()
            .filter(entry -> entry.getValue().contains(piece))
            .map(Map.Entry::getKey)
            .findAny()
            .orElse(null);
    }
}

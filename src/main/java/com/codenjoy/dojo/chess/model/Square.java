package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.services.Point;

public class Square {
    private final Element element;
    private final Point position;

    public Square(Element element, Point position) {
        this.element = element;
        this.position = position;
    }

    public ReaderEl toReaderEl() {
        return new ReaderEl(position, element);
    }
}

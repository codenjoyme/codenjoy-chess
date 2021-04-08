package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.services.Point;

public class Barrier {
    private final Element element;
    private final Point position;

    public Barrier(Point position) {
        this.element = Element.BARRIER;
        this.position = position;
    }

    public ReaderEl toReaderEl() {
        return new ReaderEl(position, element);
    }
}

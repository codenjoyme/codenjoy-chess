package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class ReaderEl extends PointImpl implements State<Element, Player> {
    private final Element element;

    public ReaderEl(Point position, Element element) {
        super(position);
        this.element = element;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return element;
    }
}

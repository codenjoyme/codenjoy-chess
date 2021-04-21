package com.codenjoy.dojo.chess.service;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

@SuppressWarnings("SuspiciousNameCombination")
public class Rotator {

    private final int boardSize;

    public Rotator(int boardSize) {
        this.boardSize = boardSize;
    }

    public static int countRotationTimes(Direction from, Direction to) {
        int times = 0;
        Direction direction = from;
        while (direction != to) {
            direction = direction.clockwise();
            times++;
        }
        return times % 4;
    }

    public <T extends Point> void mapPosition(T position, Direction from, Direction to) {
        rotateClockwise(position, countRotationTimes(from, to));
    }

    /**
     * Rotates position clockwise.
     *
     * @param position the position to be rotated
     * @param times how much times to rotate
     */
    public <T extends Point> void rotateClockwise(T position, int times) {
        if ((times = times % 4) == 0) {
            return;
        }
        int x = position.getX();
        int y = position.getY();
        if (times == 1) {
            position.move(y, boardSize - 1 - x);
        } else if (times == 2) {
            position.move(boardSize - 1 - x, boardSize - 1 - y);
        } else {
            position.move(boardSize - 1 - y, x);
        }
    }
}

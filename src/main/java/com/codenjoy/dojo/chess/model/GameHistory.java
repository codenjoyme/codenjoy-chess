package com.codenjoy.dojo.chess.model;

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

import java.util.ArrayList;
import java.util.List;

public class GameHistory {

    private final List<Record> records;

    public GameHistory() {
        this(new ArrayList<>());
    }

    public GameHistory(List<Record> records) {
        this.records = records;
    }

    public void add(HeroColor color, Move move) {
        add(new Record(color, move));
    }

    public void add(Record record) {
        records.add(record);
    }

    public Record getLastRecord() {
        return records.isEmpty() ? null : records.get(records.size() - 1);
    }

    @Override
    public String toString() {
        return "GameHistory{" + records + '}';
    }

    public static class Record {

        private final HeroColor color;
        private final Move move;

        public Record(HeroColor color, Move move) {
            this.color = color;
            this.move = move;
        }

        public HeroColor getColor() {
            return color;
        }

        public Move getMove() {
            return Move.decode(move.command());
        }

        @Override
        public String toString() {
            return color + ": " + move;
        }
    }
}

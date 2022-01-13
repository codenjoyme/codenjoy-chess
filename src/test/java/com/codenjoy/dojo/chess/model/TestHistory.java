package com.codenjoy.dojo.chess.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.chess.service.Event;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class TestHistory {

    private List<Record> records;

    public TestHistory() {
        this(Lists.newArrayList());
    }

    public TestHistory(List<Record> records) {
        this.records = records;
    }

    public Record getFirstWithEvent(Event event) {
        return records.stream()
                .filter(r -> r.getEvents().contains(event))
                .findFirst()
                .orElse(null);
    }

    public void add(HeroColor color, Move move, List<Event> events) {
        if (events == null || events.size() == 0) {
            records.add(new Record(color, move));
        } else {
            records.add(new Record(color, move, events.toArray(Event[]::new)));
        }
    }

    public void add(HeroColor color, Move move, Event... events) {
        records.add(new Record(color, move, events));
    }

    @Override
    public String toString() {
        return records.stream()
                .map(Record::toString)
                .collect(Collectors.joining("\n"));
    }

    public static class Record {
        private final HeroColor color;
        private final Move move;
        private final List<Event> events;

        private Record(HeroColor color, Move move, Event... events) {
            this.color = color;
            this.move = move;
            this.events = Lists.newArrayList(events);
        }

        public HeroColor getColor() {
            return color;
        }

        public Move getMove() {
            return Move.decode(move.command());
        }

        public List<Event> getEvents() {
            return events;
        }

        public boolean hasEvents() {
            return events.size() > 0;
        }

        @Override
        public String toString() {
            return color + ": " + move + "; Events: " + events.toString();
        }
    }
}

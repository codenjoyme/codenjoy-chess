package com.codenjoy.dojo.chess.common;

import com.codenjoy.dojo.chess.engine.model.Color;
import com.codenjoy.dojo.chess.engine.model.Event;
import com.codenjoy.dojo.chess.engine.service.Move;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class TestHistory {
    private final List<Record> records;

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

    public void add(Color color, Move move, List<Event> events) {
        if (events == null || events.size() == 0) {
            records.add(new Record(color, move));
        } else {
            records.add(new Record(color, move, events.toArray(Event[]::new)));
        }
    }

    public void add(Color color, Move move, Event... events) {
        records.add(new Record(color, move, events));
    }

    @Override
    public String toString() {
        return records.stream()
                .map(Record::toString)
                .collect(Collectors.joining("\n"));
    }

    public static class Record {
        private final Color color;
        private final Move move;
        private final List<Event> events;

        private Record(Color color, Move move, Event... events) {
            this.color = color;
            this.move = move;
            this.events = Lists.newArrayList(events);
        }

        public Color getColor() {
            return color;
        }

        public Move getMove() {
            return Move.decode(move.command());
        }

        public List<Event> getEvents() {
            return events;
        }

        @SuppressWarnings("unused")
        public boolean hasEvents() {
            return events.size() > 0;
        }

        @Override
        public String toString() {
            return color + ": " + move + "; Events: " + events.toString();
        }
    }
}
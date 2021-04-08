package com.codenjoy.dojo.chess.model;

import com.google.common.collect.Lists;

import java.util.List;

public class GameHistory {

    private final List<HistoryRecord> records = Lists.newArrayList();

    public void add(Color color, Move move) {
        add(new HistoryRecord(color, move));
    }

    public void add(HistoryRecord record) {
        records.add(record);
    }

    public Move getLastMoveOf(Color color) {
        for (int i = records.size() - 1; i >= 0; i--) {
            HistoryRecord record = records.get(i);
            if (record.color == color) {
                return record.move;
            }
        }
        return null;
    }

    public HistoryRecord getLastRecord() {
        return records.isEmpty() ? null : records.get(records.size() - 1);
    }

    public static class HistoryRecord {

        private final Color color;
        private final Move move;

        public HistoryRecord(Color color, Move move) {
            this.color = color;
            this.move = move;
        }

        public Color getColor() {
            return color;
        }

        public Move getMove() {
            return Move.decode(move.command());
        }
    }
}

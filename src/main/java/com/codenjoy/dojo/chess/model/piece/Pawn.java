package com.codenjoy.dojo.chess.model.piece;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.chess.model.Board;
import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.chess.model.Color.*;

public class Pawn extends Piece {
    private static final Map<Color, Color> enPassantOpponentColors = new HashMap<>() {{
       put(WHITE, BLACK);
       put(BLACK, WHITE);
       put(RED, BLUE);
       put(BLUE, RED);
    }};


    private boolean moved;

    public Pawn(Color color, Board board, Point position) {
        super(PieceType.PAWN, color, board, position);
    }

    @Override
    public void move(Move move) {
        Point p;
        if ((p = getAttackDirection().clockwise().change(getAttackDirection().change(position))).equals(move.getTo())) {
            if (board.getAt(p).isEmpty()) {
                Piece piece = board.getAt(getAttackDirection().inverted().change(p)).orElse(null);
                piece.setAlive(false);
            }
        }
        if ((p = getAttackDirection().counterClockwise().change(getAttackDirection().change(position))).equals(move.getTo())) {
            if (board.getAt(p).isEmpty()) {
                Piece piece = board.getAt(getAttackDirection().inverted().change(p)).orElse(null);
                piece.setAlive(false);
            }
        }
        super.move(move);
        moved = true;
    }

    @Override
    public List<Move> getAvailableMoves() {
        List<Move> moves = Lists.newArrayList();
        Point step = getAttackDirection().change(position);
        if (board.getAt(step).isEmpty()) {
            moves.add(Move.from(position).to(step));
            if (step.getY() == 0 || step.getY() == board.getSize() - 1) {
                for (PieceType ptype : PieceType.values()) {
                    if (ptype != PieceType.KING) {
                        moves.add(Move.from(position).to(step).promotion(ptype));
                    }
                }
            }
            if (!moved) {
                step = getAttackDirection().change(step);
                if (board.getAt(step).isEmpty()) {
                    moves.add(Move.from(position).to(step));
                }
            }
        }
        // en passant
        Move enPassantMove = board.getHistory().getLastMoveOf(enPassantOpponentColors.get(color));
        if (enPassantMove != null) {
            if (board.getAt(enPassantMove.getTo()).get() instanceof Pawn
                    && getAttackDirection().counterClockwise().change(position).equals(enPassantMove.getTo())
                    && getAttackDirection().counterClockwise().change(getAttackDirection().change(getAttackDirection().change(position))).equals(enPassantMove.getFrom())) {
                moves.add(Move.from(position).to(getAttackDirection().counterClockwise().change(getAttackDirection().change(position))));
            }
            if (board.getAt(enPassantMove.getTo()).get() instanceof Pawn
                    && getAttackDirection().clockwise().change(position).equals(enPassantMove.getTo())
                    && getAttackDirection().clockwise().change(getAttackDirection().change(getAttackDirection().change(position))).equals(enPassantMove.getFrom())) {
                moves.add(Move.from(position).to(getAttackDirection().clockwise().change(getAttackDirection().change(position))));
            }
        }
        step = getAttackDirection().change(position);
        if (board.getAt(getAttackDirection().clockwise().change(step)).isPresent() && board.getAt(getAttackDirection().clockwise().change(step)).get().getColor() != color) {
            moves.add(Move.from(position).to(getAttackDirection().clockwise().change(step)));
        }
        if (board.getAt(getAttackDirection().counterClockwise().change(step)).isPresent() && board.getAt(getAttackDirection().counterClockwise().change(step)).get().getColor() != color) {
            moves.add(Move.from(position).to(getAttackDirection().counterClockwise().change(step)));
        }
        return moves;
    }
}
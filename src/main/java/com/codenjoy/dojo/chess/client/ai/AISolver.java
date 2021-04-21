package com.codenjoy.dojo.chess.client.ai;

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

import com.codenjoy.dojo.chess.client.Board;
import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.model.item.piece.Pawn;
import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.chess.service.Chess;
import com.codenjoy.dojo.chess.service.GameBoard;
import com.codenjoy.dojo.chess.service.Rotator;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AISolver implements Solver<Board> {

    // direction of attack of each player on the boards they received
    private static final Direction SOLVER_ATTACK_DIRECTION = Chess.getDefaultAttackDirection();
    private static final String REQUEST_FOR_COLOR = "ACT";

    private final Dice dice;

    private GameBoard board;
    private Color color;
    private String state;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    /*
     * Server sends boards to the players in such a way
     * that the direction of attack of the player's pieces is up.
     * The direction of attack for each color is different.
     * This method is used to rotate the board
     * so that it returns to the same position as on the server.
     * This will allow you to use server-side code to simplify the writing of this solver.
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private static String rotate(Color color, String board) {
        int times = Rotator.countRotationTimes(SOLVER_ATTACK_DIRECTION, color.getAttackDirection());
        String map = LevelUtils.clear(board);
        if (times == 0) {
            return map;
        }
        int size = (int) Math.sqrt(map.length());
        LengthToXY xy = new LengthToXY(size);
        char[] rotated = new char[map.length()];
        for (int i = 0; i < map.length(); i++) {
            Point pt = xy.getXY(i);
            int x = pt.getX();
            int y = pt.getY();
            if (times == 1) {
                rotated[xy.getLength(y, size - 1 - x)] = map.charAt(i);
            } else if (times == 2) {
                rotated[xy.getLength(size - 1 - x, size - 1 - y)] = map.charAt(i);
            } else {
                rotated[xy.getLength(size - 1 - y, x)] = map.charAt(i);
            }
        }
        StringBuilder result = new StringBuilder();
        for (char ch : rotated) {
            result.append(ch);
        }
        return result.toString();
    }

    private static String retrieveMap(Board board) {
        return LevelUtils.clear(board.boardAsString());
    }

    @Override
    public String get(final Board newState) {
        GameBoard receivedBoard = new GameBoard(retrieveMap(newState));

        // server sends board with only the player's color pieces
        // after the player has sent a request for color identification
        if (receivedBoard.getColors().size() == 1) {
            // identify color and don't update board's state
            color = receivedBoard.getColors().get(0);
        } else {
            // we can not create a GameBoard because at the time we may not know the color
            // but a valid board state received and we should save it for further GameBoard creation
            state = retrieveMap(newState);
            if (color == null) {
                // if color is null then we should ask server to send us board
                // only with the player's color pieces for further color identification
                return REQUEST_FOR_COLOR;
            }
        }

        // create board from state
        board = new GameBoard(
                // for creating this we should rotate back the board as it is on server
                rotate(color, state)
        );

        // get all available moves without castling, en passant and promotion
        // because we need to know many things for that,
        // such as is piece moved or not, when it was moved, etc
        List<Move> availableMoves = getPieces(color).stream()
                .map(Piece::getAvailableMoves)
                .flatMap(Collection::stream)
                .filter(this::notCastling)
                .filter(this::notEnPassant)
                .filter(this::withoutPromotion)
                .collect(Collectors.toList());

        // nowhere to go
        if (availableMoves.isEmpty()) {
            return "";
        }

        // check, whether are attacking enemy's pieces moves among the available moves
        List<Move> attackMoves = availableMoves.stream()
                .filter(m -> this.board.getPieceAt(m.getTo()).isPresent())
                .collect(Collectors.toList());

        // prefer to attack if possible
        Move decision = attackMoves.size() > 0
                ? attackMoves.get(dice.next(attackMoves.size()))
                : availableMoves.get(dice.next(availableMoves.size()));

        // since the board was rotated back, the move we decided to make is rotated too,
        // so we need to make move's coordinates look like they was for received board
        // in accordance with player's color
        decision = mapMove(decision);

        return String.format(
                "ACT(%d,%d,%d,%d)",
                decision.getFrom().getX(),
                decision.getFrom().getY(),
                decision.getTo().getX(),
                decision.getTo().getY()
        );
    }

    private Move mapMove(Move move) {
        Direction direction = color.getAttackDirection();
        Rotator rotator = new Rotator(board.getSize());
        Point from = move.getFrom();
        Point to = move.getTo();
        rotator.mapPosition(from, direction, SOLVER_ATTACK_DIRECTION);
        rotator.mapPosition(to, direction, SOLVER_ATTACK_DIRECTION);
        return Move.from(from).to(to).promotion(move.getPromotion());
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean notCastling(Move move) {
        Piece piece1 = board.getPieceAt(move.getFrom()).orElse(null);
        Piece piece2 = board.getPieceAt(move.getTo()).orElse(null);
        return piece1 == null
                || piece2 == null
                || piece1.getColor() != piece2.getColor()
                || piece1.getType() != Piece.Type.KING
                || piece2.getType() != Piece.Type.ROOK;
    }

    private boolean notEnPassant(Move move) {
        Piece piece = board.getPieceAt(move.getFrom()).orElse(null);
        if (piece == null || piece.getType() != Piece.Type.PAWN || board.getPieceAt(move.getTo()).isPresent()) {
            return true;
        }
        return !((Pawn) piece).isEnPassant(move);
    }

    private boolean withoutPromotion(Move move) {
        return !move.withPromotion();
    }

    private List<Piece> getPieces(Color color) {
        return board.getPieces().stream()
                .filter(p -> p.getColor() == color)
                .collect(Collectors.toList());
    }
}

package com.codenjoy.dojo.chess.model.item.piece;

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


import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.service.GameBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// https://en.wikipedia.org/wiki/Pawn_(chess)
public class Pawn extends Piece {
    private static final int LINE_OF_PROMOTION = 7;

    public Pawn(Color color, GameBoard board, Point position) {
        super(Type.PAWN, color, board, position);
    }

    /**
     * Checks if promotion is able at destination point or not.
     *
     * TODO: Needs testing
     *
     * @param attackDirection the direction of attack of pawn
     * @param destination     the destination point of pawn
     * @param boardSize       the size of a board
     * @return true, if promotion is able, false otherwise
     */
    private static boolean isPromotionAble(Direction attackDirection, Point destination, int boardSize) {
        switch (attackDirection) {
            case LEFT:
                return destination.getX() == (boardSize - 1) - LINE_OF_PROMOTION || destination.getX() == 0;
            case RIGHT:
                return destination.getX() == LINE_OF_PROMOTION || destination.getX() == boardSize - 1;
            case UP:
                return destination.getY() == LINE_OF_PROMOTION || destination.getY() == boardSize - 1;
            case DOWN:
                return destination.getY() == (boardSize - 1) - LINE_OF_PROMOTION || destination.getY() == 0;
            default:
                throw new IllegalArgumentException("Invalid attack direction");
        }
    }

    /**
     * https://en.wikipedia.org/wiki/Promotion_(chess)
     * The method returns all available promotion moves from pawn's position.
     *
     * @param board           a chess board
     * @param position        a position of a pawn
     * @param attackDirection the direction of attack of the pawn
     * @return all available promotion moves
     */
    private static List<Move> promotionMoves(GameBoard board, Point position, Direction attackDirection) {
        Point destination = attackDirection.change(position);
        if (!isPromotionAble(attackDirection, destination, board.getSize())) {
            return Lists.newArrayList();
        }
        return Arrays.stream(Type.values())
                .filter(type -> type != Type.KING)
                .map(type -> Move.from(position).to(destination).promotion(type))
                .collect(Collectors.toList());
    }

    /**
     * The method calculates all available moves for a pawn in accordance
     * with described circumstances.
     *
     * @param board           a chess board
     * @param position        a position of a pawn
     * @param color           a color of the pawn
     * @param moved           did the pawn move or not
     * @param attackDirection direction of attack of the pawn
     * @return all available moves
     */
    public static List<Move> availableMoves(GameBoard board,
                                            Point position,
                                            Color color,
                                            boolean moved,
                                            Direction attackDirection) {
        List<Move> moves = Lists.newArrayList();
        moves.addAll(basicMoves(board, position, attackDirection, moved));
        moves.addAll(promotionMoves(board, position, attackDirection));
        moves.addAll(enPassants(board, position, attackDirection, color));
        moves.addAll(attackMoves(board, position, attackDirection, color));
        return moves;
    }

    /**
     * The method calculates all available basic moves for a pawn,
     * such as one step forward and two steps forward from start position.
     *
     * @param board           a chess board
     * @param position        a position of a pawn
     * @param attackDirection direction of attack of the pawn
     * @param moved           did the pawn move or not
     * @return all available basic moves
     */
    private static List<Move> basicMoves(GameBoard board, Point position, Direction attackDirection, boolean moved) {
        Point destination = attackDirection.change(position);
        if (board.getPieceAt(destination).isPresent() || !board.isInBounds(destination)) {
            return Lists.newArrayList();
        }
        List<Move> moves = Lists.newArrayList();

        // basic move 1 square forward
        moves.add(Move.from(position).to(destination));

        if (!moved) {
            // 2 squares forward move from start position
            destination = attackDirection.change(destination);
            if (board.isInBounds(destination) && board.getPieceAt(destination).isEmpty()) {
                moves.add(Move.from(position).to(destination));
            }
        }

        return moves;
    }

    /**
     * The method calculates all available moves,
     * where enemy's pieces could be taken.
     *
     * @param board           a chess board
     * @param position        a position of a pawn
     * @param attackDirection direction of attack of the pawn
     * @param color           a color of the pawn
     * @return all available attack moves
     */
    // TODO 2x2 should not take pieces of friendly color
    private static List<Move> attackMoves(GameBoard board, Point position, Direction attackDirection, Color color) {
        List<Move> moves = Lists.newArrayList();
        attackMove(board, position, attackDirection, color, true).ifPresent(moves::add);
        attackMove(board, position, attackDirection, color, false).ifPresent(moves::add);
        return moves;
    }

    /**
     * The method calculates an attack move in specific direction if it is possible.
     * A pawn has an attack direction that simply describes the direction where pawn can move.
     * But a pawn is able to take enemy's piece diagonally, in accordance with it's direction of attack.
     * This method accepts a {@param clockwise} that describes diagonal to check.
     * <p>
     * For example: there is a pawn which can move up, but also it can take enemy's pieces from
     * up-right and up-left if they are there. So to check up-right case, clockwise should be set true
     * and for up-left it should be false.
     *
     * @param board           a chess board
     * @param position        a position of a pawn
     * @param attackDirection direction of attack of the pawn
     * @param color           a color of the pawn
     * @param clockwise       should method checks clockwise diagonal step or counterclockwise
     * @return move if enemy's piece can be taken or Optional.empty() otherwise
     */
    private static Optional<Move> attackMove(GameBoard board, Point position, Direction attackDirection, Color color, boolean clockwise) {
        Point destination = attackDirection.change(position);
        destination = clockwise
                ? attackDirection.clockwise().change(destination)
                : attackDirection.counterClockwise().change(destination);
        if (board.getPieceAt(destination).isEmpty()) {
            return Optional.empty();
        }
        Piece enemyPiece = board.getPieceAt(destination).get();
        if (enemyPiece.getColor() == color) {
            return Optional.empty();
        }
        return Optional.ofNullable(Move.from(position).to(destination));
    }

    /**
     * https://en.wikipedia.org/wiki/En_passant
     * The method calculates all available "en passant" moves
     * where enemy's piece could be taken.
     *
     * @param board           a chess board
     * @param position        a position of a pawn
     * @param attackDirection direction of attack of the pawn
     * @param color           a color of the pawn
     * @return all available "en passant" moves
     */
    private static List<Move> enPassants(GameBoard board, Point position, Direction attackDirection, Color color) {
        List<Move> enPassants = Lists.newArrayList();
        enPassant(board, position, attackDirection, color, true).ifPresent(enPassants::add);
        enPassant(board, position, attackDirection, color, false).ifPresent(enPassants::add);
        return enPassants;
    }

    /**
     * The method calculates is there an "en passant" move in specific direction.
     * Look {@link Pawn#attackMove(GameBoard, Point, Direction, Color, boolean)}
     * for clarification about clockwise parameter.
     *
     * @param board           a chess board
     * @param position        a position of a pawn
     * @param attackDirection direction of attack of the pawn
     * @param color           a color of the pawn
     * @param clockwise       should method checks clockwise diagonal "en passant" or counterclockwise
     * @return move if "en passant" in this direction is able, Optional.empty() otherwise
     */
    private static Optional<Move> enPassant(GameBoard board, Point position, Direction attackDirection, Color color, boolean clockwise) {
        boolean enPassantAvailable = board.getColors().stream()
                .filter(c -> !c.equals(color))
                .map(board::getLastMoveOf)
                .anyMatch(m -> isAbleForEnPassant(m, board, position, attackDirection, clockwise));
        if (enPassantAvailable) {
            Point attackPosition = clockwise
                    ? attackDirection.change(attackDirection.clockwise().change(position))
                    : attackDirection.change(attackDirection.counterClockwise().change(position));
            return Optional.ofNullable(Move.from(position).to(attackPosition));
        }
        return Optional.empty();
    }

    /**
     * The method checks if a move of enemy's pawn allows take it
     * "en passant" by a pawn in described circumstances or not.
     *
     * TODO: Needs testing
     *
     * @param move            a movement possibly suitable for allowing to take a piece which made it
     * @param board           a chess board
     * @param position        a position of a pawn, which should make the attack
     * @param attackDirection a direction of attack of the pawn described above
     * @param clockwise       should method checks clockwise diagonal "en passant" or counterclockwise
     * @return true, if "en passant" is executable, false otherwise
     */
    private static boolean isAbleForEnPassant(Move move, GameBoard board, Point position, Direction attackDirection, boolean clockwise) {
        if (move == null) {
            return false;
        }
        // checks if the move is made by pawn
        Point moveDestination = move.getTo();
        boolean pawnMove = board.getPieceAt(moveDestination)
                .map(p -> p.getType() == Type.PAWN)
                .orElse(false);
        if (!pawnMove) {
            return false;
        }
        // checks if the pawn made a two step forward move
        Point pawnShouldStartFrom = attackDirection.change(attackDirection.change(moveDestination));
        if (!pawnShouldStartFrom.equals(move.getFrom())) {
            return false;
        }
        // checks if the pawn located to the left or right of
        // specified position relative to the direction of attack
        return clockwise
                ? attackDirection.clockwise().change(position).equals(moveDestination)
                : attackDirection.counterClockwise().change(position).equals(moveDestination);
    }

    @Override
    public boolean move(Move move) {
        if (enPassants(board, position, attackDirection, color).contains(move)) {
            Point destination = move.getTo();
            Piece enemyPawn = board.getPieceAt(attackDirection.inverted().change(destination))
                    .orElseThrow(IllegalStateException::new);
            enemyPawn.setAlive(false);
        }
        return super.move(move);
    }

    @Override
    public List<Move> getAvailableMoves() {
        return availableMoves(board, position, color, moved, attackDirection);
    }

    @Override
    public boolean isAttacks(Point position) {
        Point stepForward = attackDirection.change(this.position);
        Point clockwiseAttack = attackDirection.clockwise().change(stepForward);
        Point counterclockwiseAttack = attackDirection.counterClockwise().change(stepForward);
        return position.equals(clockwiseAttack) || position.equals(counterclockwiseAttack);
    }

    /**
     * The method checks if a move is "en passant" move of the piece or not.
     *
     * @param move the move
     * @return true, if the move is "en passant", false otherwise
     */
    public boolean isEnPassant(Move move) {
        return enPassants(board, position, attackDirection, color).contains(move);
    }
}
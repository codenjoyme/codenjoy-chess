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

import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.chess.service.Event;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.joystick.NoDirectionJoystick;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hero extends PlayerHero<Field> implements NoDirectionJoystick {

    private static final Logger LOGGER = LoggerFactory.getLogger(Hero.class);

    private final HeroColor color;
    private final List<Event> events = new ArrayList<>();
    private boolean askedForColor = false;
    private Move lastMove;
    private Move action;

    public Hero(HeroColor color) {
        this.color = color;
    }

    @Override
    public void act(int... codes) {
        if (field.getCurrentColor() != getColor()) {
            return;
        }
        if (codes.length == 0) {
            askedForColor = true;
            return;
        }
        action = Move.decode(codes);
        if (action == null) {
            LOGGER.debug(
                    "Hero with color {} received invalid action parameters: {}",
                    getColor(), Arrays.toString(codes)
            );
            return;
        }
        action = mapMove(action);
    }

    private Move mapMove(Move move) {
        Rotator rotator = field.getRotator();
        Direction directionFrom = Chess.getDefaultAttackDirection();
        Direction directionTo = color.getAttackDirection();
        Point from = move.getFrom().copy();
        Point to = move.getTo().copy();
        rotator.mapPosition(from, directionFrom, directionTo);
        rotator.mapPosition(to, directionFrom, directionTo);
        return Move.from(from).to(to).promotion(move.getPromotion());
    }

    private void clearBeforeTick() {
        events.clear();
        lastMove = null;
        askedForColor = false;
    }

    @Override
    public void tick() {
        clearBeforeTick();
        if (askedForColor || color != field.getCurrentColor() || action == null) {
            return;
        }
        List<Piece> piecesBeforeMove = field.getBoard().getAlivePieces();
        if (field.getBoard().tryMove(color, action)) {
            lastMove = action;
            CollectionUtils.subtract(piecesBeforeMove, field.getBoard().getAlivePieces()).stream()
                    .filter(p -> p.getColor() != color) // exclude pawn's disappearance in case of promotion
                    .map(this::eventOfTaken)
                    .forEach(events::add);
        } else {
            events.add(Event.WRONG_MOVE);
        }
        action = null;
    }

    private Event eventOfTaken(Piece piece) {
        if (piece.isAlive()) {
            throw new IllegalArgumentException("Piece should be taken");
        }
        Piece.Type type = piece.getType();
        switch (type) {
            case KING:
                return Event.KING_TAKEN;
            case QUEEN:
                return Event.QUEEN_TAKEN;
            case KNIGHT:
                return Event.KNIGHT_TAKEN;
            case BISHOP:
                return Event.BISHOP_TAKEN;
            case ROOK:
                return Event.ROOK_TAKEN;
            case PAWN:
                return Event.PAWN_TAKEN;
            default:
                throw new IllegalArgumentException("Unknown piece type");
        }
    }

    public HeroColor getColor() {
        return color;
    }

    public List<Event> getEvents() {
        return events;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public boolean isAlive() {
        return field.getBoard().isAlive(color);
    }

    public boolean isWinner() {
        return field.getBoard().isWinner(color);
    }

    public boolean askedForColor() {
        if (!askedForColor) {
            return false;
        }
        askedForColor = false;
        return true;
    }
}

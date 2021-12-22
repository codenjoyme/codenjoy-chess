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

import com.codenjoy.dojo.chess.TestGameSettings;
import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.chess.service.Event;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public abstract class AbstractGameTest {
    private final Map<HeroColor, Player> players = new LinkedHashMap<>();
    private final Map<Player, EventListener> listeners = new LinkedHashMap<>();

    private String board;

    protected Chess game;
    protected Dice dice;
    protected PrinterFactory printerFactory;
    protected GameSettings settings;
    protected TestHistory history;

    public static String classicBoard() {
        return "rkbqwbkr\n" +
                "pppppppp\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n";
    }

    @Before
    public void setup() {
        dice = mock(Dice.class);
        printerFactory = new PrinterFactoryImpl();
        settings = new TestGameSettings();
        history = new TestHistory();
    }

    protected void reset() {
        setup();
        givenFl(board);
    }

    public void assrtPl(String expected) {
        assertEquals(expected,
                players.values().stream()
                        .map(player -> playerStatus(player))
                        .collect(joining("\n---------------\n")));
    }

    private String playerStatus(Player player) {
        return String.format(
                "Color  = %s\n" +
                "Alive  = %s\n" +
                "Active = %s\n" +
                "Win    = %s\n" +
                "Stay   = %s\n" +
                "Leave  = %s\n" +
                "Asked  = %s\n" +
                "Last   = %s",
                player.getColor(),
                player.isAlive(),
                player.isActive(),
                player.isWin(),
                player.wantToStay(),
                player.shouldLeave(),
                player.askedForColor(),
                player.isLastWinnerOnBoard());
    }

    protected void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    protected void neverFired(HeroColor color, Event event) {
        EventListener eventListener = listeners.get(players.get(color));
        verify(eventListener, never()).event(event);
    }

    protected void neverFired(Event event) {
        for (Map.Entry<Player, EventListener> entry : listeners.entrySet()) {
            verify(entry.getValue(), never()).event(event);
        }
    }

    protected void fired(HeroColor color, int times, Event event) {
        EventListener eventListener = listeners.get(players.get(color));
        verify(eventListener, times(times)).event(event);
    }

    protected void fired(HeroColor color, Event event) {
        fired(color, 1, event);
    }

    protected void fired(HeroColor color, Event... events) {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        EventListener eventListener = listeners.get(players.get(color));
        verify(eventListener, times(events.length)).event(captor.capture());
        assertEquals(Arrays.asList(events), captor.getAllValues());
    }

    protected void givenFl(String board) {
        this.board = board;
        players.clear();
        listeners.clear();
        Level level = new Level(board);
        game = new Chess(level, dice, settings);
        for (int i = 0; i < level.presentedColors().size(); i++) {
            EventListener listener = mock(EventListener.class);
            Player player = new Player(listener, settings);
            game.newGame(player);
            players.put(player.getColor(), player);
            listeners.put(player, listener);
        }
    }

    protected void assertE(String expected) {
        assertE(expected, HeroColor.WHITE);
    }

    protected void assertE(String expected, HeroColor playerColor) {
        assertEquals(expected, getBoardState(playerColor));
    }

    protected void act(HeroColor color, int... codes) {
        act(color, true, codes);
    }

    protected void act(HeroColor color, boolean withTick, int... codes) {
        players.get(color).getHero().act(codes);
        if (withTick) {
            game.tick();
        }
    }

    protected void move(HeroColor color, Move move) {
        move(color, move, true, true);
    }

    protected void move(HeroColor color, Move move, boolean withTick) {
        move(color, move, true, withTick);
    }

    protected void move(HeroColor color, final Move move, boolean notRotatedBoard, boolean withTick) {
        Move action = move;
        if (notRotatedBoard) {
            Rotator rotator = game.getRotator();
            Point from = move.getFrom();
            Point to = move.getTo();
            rotator.mapPosition(from, color.getAttackDirection(), Chess.getDefaultAttackDirection());
            rotator.mapPosition(to, color.getAttackDirection(), Chess.getDefaultAttackDirection());
            action = Move.from(from).to(to).promotion(move.getPromotion());
        }
        players.get(color).getHero().act(action.command());
        history.add(color, move, getAllFiredEvents());
        if (withTick) {
            game.tick();
        }
    }

    protected List<Event> getAllFiredEvents() {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        List<Event> firedEvents = Lists.newArrayList();
        listeners.forEach((plr, lstnr) -> {
            try {
                // TODO проверить, скорее всего будет тащить за собой все случившиеся прошлые ивенты
                verify(lstnr).event(captor.capture());
            } catch (AssertionError err) {
                // just want to fill captor
            }
            firedEvents.addAll(captor.getAllValues());
        });
        return firedEvents;
    }

    protected Piece getPieceAt(int x, int y) {
        return getPieceAt(new PointImpl(x, y));
    }

    protected Piece getPieceAt(Point position) {
        return game.getBoard().getPieces().stream()
                .filter(p -> p.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    protected int getBoardSize() {
        return game.getBoard().getSize();
    }

    protected String getBoardState(HeroColor color) {
        return (String) printerFactory.getPrinter(
                game.reader(), players.get(color)).print();
    }
}

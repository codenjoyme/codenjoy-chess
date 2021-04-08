package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.chess.service.Event;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SuppressWarnings({"rawtypes", "unused", "unchecked", "SpellCheckingInspection"})
public abstract class AbstractGameTest {
    private final Map<Color, Player> players = new HashMap<>();
    private final Map<Player, EventListener> listeners = new HashMap<>();

    protected Chess game;
    private String board;
    protected Dice dice;
    protected PrinterFactory printerFactory;
    protected GameSettings settings;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        printerFactory = new PrinterFactoryImpl();
        settings = new GameSettings();
    }

    protected void reset() {
        setup();
        givenFl(board);
    }

    protected void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    public void neverFired(Color color, Event event) {
        EventListener eventListener = listeners.get(players.get(color));
        verify(eventListener, never()).event(event);
    }

    public void fired(Color color, int times, Event event) {
        EventListener eventListener = listeners.get(players.get(color));
        verify(eventListener, times(times)).event(event);
    }

    public void fired(Color color, Event event) {
        fired(color, 1, event);
    }

    public void fired(Color color, Event... events) {
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

    protected GameSet getGameSet(Color color) {
        return players.get(color).getGameSet();
    }

    protected void assertE(String expected) {
        assertE(expected, Color.WHITE);
    }

    protected void assertE(String expected, Color playerColor) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), players.get(playerColor)).print());
    }

    protected String classicBoard() {
        return "rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR";
    }

    protected void move(Color color, Move move) {
        players.get(color).getHero().act(move.command());
        game.tick();
    }

    protected Piece getPieceAt(int x, int y) {
        return getPieceAt(new PointImpl(x, y));
    }

    protected Piece getPieceAt(Point position) {
        return game.getPieces().stream()
                .filter(p -> p.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }
}

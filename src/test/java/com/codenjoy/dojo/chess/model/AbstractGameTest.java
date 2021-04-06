package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.chess.service.Event;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
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

public abstract class AbstractGameTest {
    private Map<Color, Player> players = new HashMap<>();
    private Map<Player, EventListener> listeners = new HashMap<>();

    protected Chess game;
    protected Dice dice;
    protected PrinterFactory printerFactory;
    protected GameSettings settings;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        printerFactory = new PrinterFactoryImpl();
        settings = new GameSettings();
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
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), players.values().iterator().next()).print());
    }

    protected void classicBoard() {
        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
    }

    protected void move(Color color, Move move) {
        players.get(color).getHero().act(move.command());
        game.tick();
    }
}

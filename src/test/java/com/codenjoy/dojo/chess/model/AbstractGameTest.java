package com.codenjoy.dojo.chess.model;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public abstract class AbstractGameTest {
    protected Chess game;
    protected Dice dice;
    protected EventListener listener1;
    protected EventListener listener2;
    protected Player player1;
    protected Player player2;
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

    public void neverFired(EventListener listener, Event event) {
        verify(listener, never()).event(event);
    }

    public void fired(EventListener listener, Event event) {
        fired(listener, 1, event);
    }

    public void fired(EventListener listener, int times, Event event) {
        verify(listener, times(times)).event(event);
    }

    public void fired(EventListener listener, Event... events) {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener, times(events.length)).event(captor.capture());
        assertEquals(Arrays.asList(events), captor.getAllValues());
    }

    protected void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        game = new Chess(level, dice, settings);
    }

    protected void twoPlayers() {
        listener1 = mock(EventListener.class);
        player1 = new Player(listener1, settings);
        game.newGame(player1);

        listener2 = mock(EventListener.class);
        player2 = new Player(listener2, settings);
        game.newGame(player2);
    }

    protected void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), player1).print());
    }

    protected void classicBoardAnd2Players() {
        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        twoPlayers();
    }
}

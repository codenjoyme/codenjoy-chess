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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public abstract class AbstractGameTest {
    protected Chess game;
    protected Dice dice;
    protected EventListener whiteListener;
    protected EventListener blackListener;
    protected Player whitePlayer;
    protected Player blackPlayer;
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
        Level level = new Level(board);
        game = new Chess(level, dice, settings);
    }

    protected void twoPlayers() {
        whiteListener = mock(EventListener.class);
        whitePlayer = new Player(whiteListener, settings);
        game.newGame(whitePlayer);

        blackListener = mock(EventListener.class);
        blackPlayer = new Player(blackListener, settings);
        game.newGame(blackPlayer);
    }

    protected void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), whitePlayer).print());
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

    protected void move(Player player, Move move) {
        player.getHero().act(move.command());
        game.tick();
    }
}

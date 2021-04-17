package com.codenjoy.dojo.chess.engine.model;

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


import com.codenjoy.dojo.chess.common.AbstractGameTest;
import com.codenjoy.dojo.chess.engine.service.GameSettings;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;

import static com.codenjoy.dojo.chess.engine.model.Color.BLACK;
import static com.codenjoy.dojo.chess.engine.model.Color.WHITE;
import static com.codenjoy.dojo.chess.engine.model.Event.*;
import static com.codenjoy.dojo.chess.engine.service.Move.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("SpellCheckingInspection")
public class GameTest extends AbstractGameTest {

    @Test
    public void shouldDrowBoardForWhites_whitePiecesAtBottom() {

        // when
        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");

        // then
        assertE("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR", WHITE);
    }

    @Test
    public void shouldDrawBoardForBlacks_blackPiecesAtBottom() {

        // when
        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");

        // then
        assertE("RKBWQBKR" +
                "PPPPPPPP" +
                "........" +
                "........" +
                "........" +
                "........" +
                "pppppppp" +
                "rkbwqbkr", BLACK);
    }

    @Test
    public void shouldFireWrongMoveEvent_whenTryingToMakeWrongMove() {

        givenFl(classicBoard());

        // when
        move(WHITE, from(4, 1).to(5, 3));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeFiredWrongMove_whenTryToMoveEnemyPiece() {

        givenFl(classicBoard());

        // when
        move(WHITE, from(0, 6).to(0, 5));

        // then
        assertE(classicBoard());
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeFiredWrongMove_whenSquareDoesntHavePiece() {

        givenFl(classicBoard());

        // when
        move(WHITE, from(1, 3).to(1, 4));

        // then
        assertE(classicBoard());
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotRenderDeadPieces() {

        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....p..." +
                "PPPPPPPP" +
                "RKBQWBKR");

        // when
        move(WHITE, from(5, 1).to(4, 2));
        move(BLACK, from(0, 6).to(0, 5));
        move(WHITE, from(4, 2).to(4, 3));

        // then
        assertE("rkbqwbkr" +
                ".ppp.ppp" +
                "p......." +
                "........" +
                "....P..." +
                "........" +
                "PPPPP.PP" +
                "RKBQWBKR");
        neverFired(WRONG_MOVE);
    }

    @Test
    public void shouldBeFiredGameOverEvent_whenKingDies() {

        givenFl("w.." +
                "W.." +
                "...");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("W.." +
                "..." +
                "...");
        fired(BLACK, GAME_OVER);
    }

    @Test
    public void shouldBeFiredWinEventForPlayer_whichKingLastsAlone() {

        givenFl("w.." +
                "W.." +
                "...");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("W.." +
                "..." +
                "...");
        fired(WHITE, WIN);
    }

    @Test
    public void allPiecesShouldDieWithTheirKing() {

        givenFl("wpp" +
                "W.." +
                "...");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("W.." +
                "..." +
                "...");
    }

    @Test
    public void shouldBeThrownIllegalArgumentException_whenTryToMakeGameSetWithoutKing() {
        Assert.assertThrows(IllegalArgumentException.class, () -> givenFl("ppp....W."));
    }

    @Test
    public void shouldNotFireWrongMoveEvent_whenPlayerDidntMakeAMoveAtHisTurn() {

        givenFl(classicBoard());

        // when whites didn't move
        game.tick();

        // then
        neverFired(WRONG_MOVE);
    }

    @Test
    public void shouldIgnoreMovesOfColor_whichWereSetWhenTheColorWasNotActive() {

        givenFl(classicBoard());
        move(WHITE, from(4, 1).to(4, 2));
        move(BLACK, from(4, 6).to(4, 5));
        move(BLACK, from(4, 5).to(4, 4), false);
        move(WHITE, from(3, 1).to(3, 2));

        // when
        game.tick();

        // then move of blacks from [4, 5] to [4, 4] should be ingored
        neverFired(WRONG_MOVE);
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "....p..." +
                "........" +
                "........" +
                "...PP..." +
                "PPP..PPP" +
                "RKBQWBKR");
    }

    @Test
    public void shouldWaitUntilPlayerMakeAMove_ifSuchOptionSetTrue() {

        givenFl(classicBoard());
        settings.bool(GameSettings.Option.WAIT_UNTIL_MAKE_A_MOVE, true);

        // when
        game.tick();

        // then should not change active color
        assertEquals(WHITE, game.getCurrentColor());
    }

    @Test
    public void shouldNotWaitUntilPlayerMakeAMove_ifSuchOptionSetFalse() {

        givenFl(classicBoard());
        settings.bool(GameSettings.Option.WAIT_UNTIL_MAKE_A_MOVE, false);

        // when
        game.tick();

        // then should change active color
        assertEquals(BLACK, game.getCurrentColor());
    }

    @Test
    public void shouldNotFireWrongMoveEvent_whenPlayerTryingToMakeAMoveNotAtHisTurn() {

        givenFl(classicBoard());
        move(WHITE, from(4, 1).to(4, 2));
        move(BLACK, from(4, 6).to(4, 5));

        // when
        move(BLACK, from(4, 5).to(4, 4));

        // then move of blacks from [4, 5] to [4, 4] should be ingored
        neverFired(WRONG_MOVE);
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "....p..." +
                "........" +
                "........" +
                "....P..." +
                "PPPP.PPP" +
                "RKBQWBKR");
    }

    @Test
    public void shouldDrawOnlyPlayerPieces_whenHeAsked() {

        givenFl(classicBoard());

        // when
        act(WHITE);

        // then
        assertE("........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR", WHITE);
        neverFired(WRONG_MOVE);
    }

    @Test
    public void shouldNotChangeActiveColor_whenPlayerAsksHisColor() {

        // given
        shouldDrawOnlyPlayerPieces_whenHeAsked();

        // then
        assertEquals(WHITE, game.getCurrentColor());
    }

    @Test
    public void shouldDrawBoardWithAllPieces_whenOneTickLastsAfterAskingColor() {

        // given
        shouldDrawOnlyPlayerPieces_whenHeAsked();

        // when
        game.tick();

        // then
        assertE(classicBoard(), WHITE);
    }
}
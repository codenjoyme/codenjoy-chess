package com.codenjoy.dojo.chess.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.chess.service.GameSettings;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.HeroColor.BLACK;
import static com.codenjoy.dojo.chess.model.Move.from;
import static com.codenjoy.dojo.chess.model.HeroColor.WHITE;
import static com.codenjoy.dojo.chess.service.Event.*;
import static com.codenjoy.dojo.chess.service.GameSettings.Option.LAST_PLAYER_STAYS;
import static org.junit.Assert.assertEquals;

public class GameTest extends AbstractGameTest {

    @Test
    public void shouldDrowBoardForWhites_whitePiecesAtBottom() {

        // when
        givenFl("rkbqwbkr\n" +
                "pppppppp\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n");

        // then
        assertE("rkbqwbkr\n" +
                "pppppppp\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n", WHITE);
    }

    @Test
    public void shouldDrawBoardForBlacks_blackPiecesAtBottom() {

        // when
        givenFl("rkbqwbkr\n" +
                "pppppppp\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n");

        // then
        assertE("RKBWQBKR\n" +
                "PPPPPPPP\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "pppppppp\n" +
                "rkbwqbkr\n", BLACK);
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

        givenFl("rkbqwbkr\n" +
                "pppp.ppp\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "....p...\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n");

        // when
        move(WHITE, from(5, 1).to(4, 2));
        move(BLACK, from(0, 6).to(0, 5));
        move(WHITE, from(4, 2).to(4, 3));

        // then
        assertE("rkbqwbkr\n" +
                ".ppp.ppp\n" +
                "p.......\n" +
                "........\n" +
                "....P...\n" +
                "........\n" +
                "PPPPP.PP\n" +
                "RKBQWBKR\n");

        neverFired(WRONG_MOVE);
    }

    @Test
    public void shouldBeFiredGameOverEvent_whenKingDies_lastLeave() {
        settings.bool(LAST_PLAYER_STAYS, false);

        givenFl("w..\n" +
                "W..\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("W..\n" +
                "...\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = false\n" +
                "Active = true\n" +
                "Win    = true\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = true\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = false\n" +
                "Active = false\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        fired(BLACK, GAME_OVER);
    }

    @Test
    public void shouldBeFiredGameOverEvent_whenKingDies_lastStay() {
        settings.bool(LAST_PLAYER_STAYS, true);

        givenFl("w..\n" +
                "W..\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("W..\n" +
                "...\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = false\n" +
                "Active = true\n" +
                "Win    = true\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = true\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = false\n" +
                "Active = false\n" +
                "Win    = false\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        fired(BLACK, GAME_OVER);
    }

    @Test
    public void shouldBeFiredWinEventForPlayer_whichKingLastsAlone_lastLeave() {
        settings.bool(LAST_PLAYER_STAYS, false);

        givenFl("w..\n" +
                "W..\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("W..\n" +
                "...\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = false\n" +
                "Active = true\n" +
                "Win    = true\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = true\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = false\n" +
                "Active = false\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        fired(WHITE, WIN);
    }

    @Test
    public void shouldBeFiredWinEventForPlayer_whichKingLastsAlone_lastStay() {
        settings.bool(LAST_PLAYER_STAYS, true);

        givenFl("w..\n" +
                "W..\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("W..\n" +
                "...\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = false\n" +
                "Active = true\n" +
                "Win    = true\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = true\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = false\n" +
                "Active = false\n" +
                "Win    = false\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        fired(WHITE, WIN);
    }

    @Test
    public void allPiecesShouldNotDieWithTheirKing_lastLeave() {
        settings.bool(LAST_PLAYER_STAYS, false);

        givenFl("wpp\n" +
                "W..\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("Wpp\n" +
                "...\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = true\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = false\n" +
                "Active = false\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");
    }

    @Test
    public void allPiecesShouldNotDieWithTheirKing_lastStay() {
        settings.bool(LAST_PLAYER_STAYS, true);

        givenFl("wpp\n" +
                "W..\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("Wpp\n" +
                "...\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = true\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = false\n" +
                "Active = false\n" +
                "Win    = false\n" +
                "Stay   = true\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");
    }

    @Test
    public void shouldNotBeThrownException_whenTryToMakeGameSetWithoutKing() {
        givenFl("ppp\n" +
                "W..\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = false\n" +
                "Active = false\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");

        // when
        move(WHITE, from(0, 1).to(0, 2));

        // then
        assertE("Wpp\n" +
                "...\n" +
                "...\n");

        assrtPl("Color  = WHITE\n" +
                "Alive  = true\n" +
                "Active = true\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false\n" +
                "---------------\n" +
                "Color  = BLACK\n" +
                "Alive  = false\n" +
                "Active = false\n" +
                "Win    = false\n" +
                "Stay   = false\n" +
                "Leave  = true\n" +
                "Asked  = false\n" +
                "Last   = false");
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
        assertE("rkbqwbkr\n" +
                "pppp.ppp\n" +
                "....p...\n" +
                "........\n" +
                "........\n" +
                "...PP...\n" +
                "PPP..PPP\n" +
                "RKBQWBKR\n");
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
        assertE("rkbqwbkr\n" +
                "pppp.ppp\n" +
                "....p...\n" +
                "........\n" +
                "........\n" +
                "....P...\n" +
                "PPPP.PPP\n" +
                "RKBQWBKR\n");
    }

    @Test
    public void shouldDrawOnlyPlayerPieces_whenHeAsked() {

        givenFl(classicBoard());

        // when
        act(WHITE);

        // then
        assertE("........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "PPPPPPPP\n" +
                "RKBQWBKR\n", WHITE);
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
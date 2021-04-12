package com.codenjoy.dojo.chess.model;

import org.junit.Ignore;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.*;
import static com.codenjoy.dojo.chess.model.Move.*;

/**
 * https://en.wikipedia.org/wiki/Four-player_chess
 * Order in this implementation: WHITE -> RED -> BLACK -> BLUE
 */
@Ignore
public class FourPlayerChessGameTest extends AbstractGameTest {

    private String fourPlayersBoard() {
        return  "   rkbwqbkr   " +
                "   pppppppp   " +
                "   ........   " +
                "IZ..........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ..........zi" +
                "   ........   " +
                "   PPPPPPPP   " +
                "   RKBQWBKR   ";
    }

    @Test
    public void shouldDrawBoardProperlyForAllPlayers() {

        // when
        givenFl(fourPlayersBoard());

        // then
        for (Color color: game.getColors()) {
            assertE(fourPlayersBoard(), color);
        }
    }

    @Test
    public void whitesShouldMoveFirst() {

        givenFl(fourPlayersBoard());

        // when
        move(BLACK, from(3, 12).to(3, 11));
        move(RED, from(1, 3).to(2, 3));
        move(BLUE, from(12, 10).to(11, 10));
        move(BLACK, from(3, 12).to(3, 11));
        move(RED, from(1, 3).to(2, 3));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(3, 1).to(3, 2));

        // then should ignore all except whites
        assertE("   rkbwqbkr   " +
                "   pppppppp   " +
                "   ........   " +
                "IZ..........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ..........zi" +
                "   P.......   " +
                "   .PPPPPPP   " +
                "   RKBQWBKR   ");
    }

    @Test
    public void redsShouldMoveSecond() {

        // given
        whitesShouldMoveFirst();

        // when
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 10).to(2, 10));

        // then should ignore all except reds
        assertE("   rkbwqbkr   " +
                "   pppppppp   " +
                "   ........   " +
                "I.Z.........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ..........zi" +
                "   P.......   " +
                "   .PPPPPPP   " +
                "   RKBQWBKR   ");
    }

    @Test
    public void blacksShouldMoveThird() {

        // given
        redsShouldMoveSecond();

        // when
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 3).to(2, 3));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(10, 12).to(10, 11));

        // then should ignore all except blacks
        assertE("   rkbwqbkr   " +
                "   ppppppp.   " +
                "   .......p   " +
                "I.Z.........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ..........zi" +
                "   P.......   " +
                "   .PPPPPPP   " +
                "   RKBQWBKR   ");
    }

    @Test
    public void bluesShouldMoveForth() {

        // given
        blacksShouldMoveThird();

        // when
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(3, 12).to(3, 11));
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 3).to(11, 3));

        // then should ignore all except blues (oh yeah)
        assertE("   rkbwqbkr   " +
                "   ppppppp.   " +
                "   .......p   " +
                "I.Z.........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ.........z.i" +
                "   P.......   " +
                "   .PPPPPPP   " +
                "   RKBQWBKR   ");
    }

    @Test
    public void andWhitesAgainShouldMoveAfterBlues() {

        // given
        bluesShouldMoveForth();

        // when
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 10).to(11, 10));
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));

        // then should ignore all except whites
        assertE("   rkbwqbkr   " +
                "   ppppppp.   " +
                "   .......p   " +
                "I.Z.........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ.........z.i" +
                "   P......P   " +
                "   .PPPPPP.   " +
                "   RKBQWBKR   ");
    }


}

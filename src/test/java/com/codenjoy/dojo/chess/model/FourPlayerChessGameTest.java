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

import com.codenjoy.dojo.chess.common.AbstractGameTest;
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
        return  "   rkbwqbkr   \n" +
                "   pppppppp   \n" +
                "   ........   \n" +
                "IZ..........zi\n" +
                "LZ..........zl\n" +
                "GZ..........zg\n" +
                "YZ..........zx\n" +
                "XZ..........zy\n" +
                "GZ..........zg\n" +
                "LZ..........zl\n" +
                "IZ..........zi\n" +
                "   ........   \n" +
                "   PPPPPPPP   \n" +
                "   RKBQWBKR   \n";
    }

    @Test
    public void shouldDrawBoardForWhites_whitePiecesAtBottom() {

        // when
        givenFl(fourPlayersBoard());

        // then
        assertE("   rkbwqbkr   \n" +
                "   pppppppp   \n" +
                "   ........   \n" +
                "IZ..........zi\n" +
                "LZ..........zl\n" +
                "GZ..........zg\n" +
                "YZ..........zx\n" +
                "XZ..........zy\n" +
                "GZ..........zg\n" +
                "LZ..........zl\n" +
                "IZ..........zi\n" +
                "   ........   \n" +
                "   PPPPPPPP   \n" +
                "   RKBQWBKR   \n", WHITE);
    }

    @Test
    public void shouldDrawBoardForReds_redsPiecesAtBottom() {

        // when
        givenFl(fourPlayersBoard());

        // then
        assertE("   ilgxygli   \n" +
                "   zzzzzzzz   \n" +
                "   ........   \n" +
                "rp..........PR\n" +
                "kp..........PK\n" +
                "bp..........PB\n" +
                "qp..........PW\n" +
                "wp..........PQ\n" +
                "bp..........PB\n" +
                "kp..........PK\n" +
                "rp..........PR\n" +
                "   ........   \n" +
                "   ZZZZZZZZ   \n" +
                "   ILGYXGLI   \n", RED);
    }

    @Test
    public void shouldDrawBoardForBlacks_blackPiecesAtBottom() {

        // when
        givenFl(fourPlayersBoard());

        // then
        assertE("   RKBWQBKR   \n" +
                "   PPPPPPPP   \n" +
                "   ........   \n" +
                "iz..........ZI\n" +
                "lz..........ZL\n" +
                "gz..........ZG\n" +
                "yz..........ZX\n" +
                "xz..........ZY\n" +
                "gz..........ZG\n" +
                "lz..........ZL\n" +
                "iz..........ZI\n" +
                "   ........   \n" +
                "   pppppppp   \n" +
                "   rkbqwbkr   \n", BLACK);
    }

    @Test
    public void shouldDrawBoardForBlues_bluesPiecesAtBottom() {

        // when
        givenFl(fourPlayersBoard());

        // then
        assertE("   ILGXYGLI   \n" +
                "   ZZZZZZZZ   \n" +
                "   ........   \n" +
                "RP..........pr\n" +
                "KP..........pk\n" +
                "BP..........pb\n" +
                "QP..........pw\n" +
                "WP..........pq\n" +
                "BP..........pb\n" +
                "KP..........pk\n" +
                "RP..........pr\n" +
                "   ........   \n" +
                "   zzzzzzzz   \n" +
                "   ilgyxgli   \n", BLUE);
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
        assertE("   rkbwqbkr   \n" +
                "   pppppppp   \n" +
                "   ........   \n" +
                "IZ..........zi\n" +
                "LZ..........zl\n" +
                "GZ..........zg\n" +
                "YZ..........zx\n" +
                "XZ..........zy\n" +
                "GZ..........zg\n" +
                "LZ..........zl\n" +
                "IZ..........zi\n" +
                "   P.......   \n" +
                "   .PPPPPPP   \n" +
                "   RKBQWBKR   \n");
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
        assertE("   rkbwqbkr   \n" +
                "   pppppppp   \n" +
                "   ........   \n" +
                "I.Z.........zi\n" +
                "LZ..........zl\n" +
                "GZ..........zg\n" +
                "YZ..........zx\n" +
                "XZ..........zy\n" +
                "GZ..........zg\n" +
                "LZ..........zl\n" +
                "IZ..........zi\n" +
                "   P.......   \n" +
                "   .PPPPPPP   \n" +
                "   RKBQWBKR   \n");
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
        assertE("   rkbwqbkr   \n" +
                "   ppppppp.   \n" +
                "   .......p   \n" +
                "I.Z.........zi\n" +
                "LZ..........zl\n" +
                "GZ..........zg\n" +
                "YZ..........zx\n" +
                "XZ..........zy\n" +
                "GZ..........zg\n" +
                "LZ..........zl\n" +
                "IZ..........zi\n" +
                "   P.......   \n" +
                "   .PPPPPPP   \n" +
                "   RKBQWBKR   \n");
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
        assertE("   rkbwqbkr   \n" +
                "   ppppppp.   \n" +
                "   .......p   \n" +
                "I.Z.........zi\n" +
                "LZ..........zl\n" +
                "GZ..........zg\n" +
                "YZ..........zx\n" +
                "XZ..........zy\n" +
                "GZ..........zg\n" +
                "LZ..........zl\n" +
                "IZ.........z.i\n" +
                "   P.......   \n" +
                "   .PPPPPPP   \n" +
                "   RKBQWBKR   \n");
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
        assertE("   rkbwqbkr   \n" +
                "   ppppppp.   \n" +
                "   .......p   \n" +
                "I.Z.........zi\n" +
                "LZ..........zl\n" +
                "GZ..........zg\n" +
                "YZ..........zx\n" +
                "XZ..........zy\n" +
                "GZ..........zg\n" +
                "LZ..........zl\n" +
                "IZ.........z.i\n" +
                "   P......P   \n" +
                "   .PPPPPP.   \n" +
                "   RKBQWBKR   \n");
    }


}

package com.codenjoy.dojo.chess.engine.service;

import com.codenjoy.dojo.chess.engine.model.item.Barrier;
import com.codenjoy.dojo.chess.engine.model.Color;
import com.codenjoy.dojo.chess.engine.model.item.Square;
import com.codenjoy.dojo.chess.engine.level.Level;
import com.codenjoy.dojo.chess.engine.model.item.piece.Piece;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

public class GameBoard {
    private final Level level;
    private final GameHistory history;
    private final List<Square> squares;
    private final List<Barrier> barriers;
    private final List<GameSet> gameSets;

    private final List<Color> used;

    private Color currentColor;

    public GameBoard(Level level) {
        this.level = level;
        this.squares = level.squares();
        this.barriers = level.barriers();
        this.gameSets = new ArrayList<>();
        this.history = new GameHistory();
        this.used = new ArrayList<>();

        for (Color color : level.presentedColors()) {
            List<Piece> pieces = Lists.newArrayList();
            for (Piece.Type pieceType : Piece.Type.values()) {
                pieces.addAll(level.pieces(color, pieceType).stream()
                        .map(position -> Piece.create(pieceType, color, this, position))
                        .collect(Collectors.toList()));
            }
            gameSets.add(new GameSet(color, this, pieces));
        }

        this.currentColor = gameSets.stream()
                .map(GameSet::getColor)
                .min(Comparator.comparingInt(Color::getPriority))
                .orElse(null);

        if (this.currentColor == null) {
            throw new IllegalArgumentException("Level " + level + " is invalid");
        }
    }

    public boolean tryMove(Color color, Move move) {
        if (color != currentColor) {
            return false;
        }
        GameSet currentGameSet = getCurrentGameSet();
        if (currentGameSet.makeMove(move)) {
            currentColor = nextColor();
            history.add(color, move);
            return true;
        }
        currentColor = nextColor();
        return false;
    }

    private Color nextColor() {
        List<GameSet> aliveSets = getAliveSets();
        aliveSets.sort(Comparator.comparingInt(s -> s.getColor().getPriority()));
        return aliveSets.stream()
                .map(GameSet::getColor)
                .filter(color -> color.getPriority() > currentColor.getPriority())
                .findAny()
                .orElse(aliveSets.get(0).getColor());
    }

    private List<GameSet> getAliveSets() {
        return gameSets.stream()
                .filter(GameSet::isKingAlive)
                .collect(Collectors.toList());
    }

    private GameSet getCurrentGameSet() {
        return gameSets.stream()
                .filter(gameSet -> gameSet.getColor() == currentColor)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public Optional<Piece> getPieceAt(Point position) {
        return gameSets.stream()
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .filter(Piece::isAlive)
                .filter(p -> p.getPosition().equals(position))
                .findAny();
    }

    public boolean isUnderAttack(Point point, Color color) {
        // TODO refactor
        boolean result = gameSets.stream()
                .filter(gameSet -> gameSet.getColor() != color)
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .map(Piece::getAvailableMoves)
                .flatMap(Collection::stream)
                .anyMatch(m -> m.getTo().equals(point));
        boolean pawnAttack = gameSets.stream()
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .filter(p -> p.getType() == Piece.Type.PAWN && p.getColor() != color)
                .anyMatch(pawn -> {
                    Direction direction = pawn.getAttackDirection();
                    Direction attack1 = direction.clockwise();
                    Direction attack2 = direction.counterClockwise();
                    return attack1.change(direction.change(pawn.getPosition())).equals(point) || attack2.change(direction.change(pawn.getPosition())).equals(point);
                });
        return result || pawnAttack;
    }

    public int getSize() {
        return level.getSize();
    }

    public List<Color> getColors() {
        return gameSets.stream()
                .map(GameSet::getColor)
                .collect(Collectors.toList());
    }

    public boolean isInBounds(Point point) {
        return !point.isOutOf(getSize());
    }

    public List<Square> getSquares() {
        return squares;
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public List<Piece> getPieces() {
        return gameSets.stream()
                .map(GameSet::getPieces)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    public List<Color> getAvailableColors() {
        return getColors().stream()
                .filter(color -> !used.contains(color))
                .collect(Collectors.toList());
    }

    public boolean setUsed(Color color) {
        return used.add(color);
    }

    public void die(Color color) {
        gameSets.stream()
                .filter(gameSet -> gameSet.getColor() == color)
                .findAny()
                .ifPresent(GameSet::die);
    }

    public GameHistory getHistory() {
        return history;
    }

    public boolean isAlive(Color color) {
        GameSet gameSet = getGameSet(color);
        if (gameSet == null) {
            throw new IllegalArgumentException("Game set with color " + color + " not exists");
        }
        return gameSet.isKingAlive();
    }

    private GameSet getGameSet(Color color) {
        return gameSets.stream()
                .filter(gameSet -> gameSet.getColor() == color)
                .findAny()
                .orElse(null);
    }

    public boolean isWinner(Color color) {
        List<GameSet> aliveSets = getAliveSets();
        return aliveSets.size() == 1 && aliveSets.get(0).getColor() == color;
    }
}

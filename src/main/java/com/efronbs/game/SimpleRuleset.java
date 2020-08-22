package com.efronbs.game;

import java.util.Locale;

/**
 * Simplest ruleset for MVP.
 * <p>
 * Rules:
 * <ol>
 *     <li>First word must overlap with the center square</li>
 *     <li>All words after the first must overlap with at least one existing word</li>
 * </ol>
 */
public class SimpleRuleset implements Ruleset {

    /**
     * Generates a game board that complies with the simple ruleset game board requirements.
     * <p>
     * Requirements:
     * <ol>
     *     <li>Game board is always a square and must have odd dimensions</li>
     * </ol>
     *
     * @param size size of the game board
     * @return the new game board
     */
    @Override
    public GameBoard generateBoard(int size) {
        if (!(size % 2 == 1)) {
            throw new IllegalArgumentException("Game board size for SimpleRuleset must be odd");
        }
        return new GameBoard(size);
    }

    // TODO refactor this method into small submethods with individual checks
    @Override
    public boolean isWordValid(
            GameBoard board,
            String word,
            Direction direction,
            int row,
            int column
    ) {
        if (!wordFitsOnBoard(board, word, direction, row, column)) {
            return false;
        }

        Point p = Point.of(row, column);
        p.decrement(direction);
        String asUpperCase = word.toUpperCase(Locale.ENGLISH);
        boolean intersect = false;
        for (Character c : asUpperCase.toCharArray()) {
            p.increment(direction);
            char existing = board.charAt(p.getRow(), p.getColumn());
            // space MUST be empty
            if (existing != 0) {
                if (!c.equals(existing)) {
                    return false;
                }
                intersect = true;
            }
        }

        // if board is not empty the placed word must intersect
        if (!board.isEmpty() && !intersect) {
            return false;
        }

        // if board is empty the placed word must go over the center square
        if (board.isEmpty()) {
            Point centerSquare = Point.of(board.getSize() / 2, board.getSize() / 2);
            Point start = Point.of(row, column);
            if (!centerSquare.isOnLine(start, p)) {
                return false;
            }
        }

        // intentionally split from final check to make adding more checks more intuitive
        return true;
    }

    private boolean wordFitsOnBoard(GameBoard board, String word, Direction direction, int row, int column) {
        // initial space is not on the board
        Point p = Point.of(row, column);
        if (!board.isInBounds(p)) {
            return false;
        }

        p.toEndOfWord(direction, word);
        if (!board.isInBounds(p)) {
            return false;
        }

        // intentionally split final check from the default "true" return to ease adding extra checks in the future.
        return true;
    }

    private boolean endInBounds(GameBoard board, Point p, String word, Direction direction) {
        Point end = p.copy();
        end.toEndOfWord(direction, word);
        return board.isInBounds(end);
    }

}

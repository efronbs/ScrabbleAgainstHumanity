package com.efronbs.game;

import java.util.Locale;

/**
 * Model object.
 */
public final class GameBoard {

    private final int size;
    private final char[][] board;
    private boolean empty;

    /**
     * Package private constructor to control creation through ruleset interface.
     *
     * @param size size of the game board. Game board is currently always square.
     */
    GameBoard(int size) {
        this.size = size;
        this.board = new char[size][size];
        this.empty = true;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return empty;
    }

    public char charAt(int row, int column) {
        if (!isInBounds(Point.of(row, column))) {
            throw new IllegalArgumentException("Requested character at invalid point (" + row + "," + column +") " +
                    " for game board of size " + size + "x" + size);
        }
        return board[row][column];
    }

    public boolean isInBounds(Point p) {
        return p.getRow() >= 0 && p.getRow() < size
                && p.getColumn() >= 0 && p.getColumn() < size;
    }

    // TODO this should probably return point value, or at least the list of letters that did not intersect???
    public void addWord(String word, Direction direction, int row, int column) {
        try {
            String asUpperCase = word.toUpperCase(Locale.ENGLISH);
            Point p = Point.of(row, column);
            for (Character c : asUpperCase.toCharArray()) {
                board[p.getRow()][p.getColumn()] = c;
                p.increment(direction);
            }
        } catch (IndexOutOfBoundsException e) {
            // since game board should be managed by a controller, bounds should always be validated before a word is
            // added. This block won't be hit frequently, so the cost of the exception is pretty low. In fact this might
            // be MORE efficient than a direct check, as we are relying
            throw new IllegalArgumentException(
                    "Word " + word +
                            " at location (" + row + "," + column + ")" +
                            " in direction " + direction.asName() +
                            " is not valid on game board:\n" + toString()
            );
        }
        empty = false;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                output.append("[")
                        .append(board[row][column] == 0 ? ' ' : board[row][column])
                        .append("]");
            }
            output.append("\n");
        }
        return output.toString();
    }
}

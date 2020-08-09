package com.efronbs.model;

import java.util.Locale;

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

    // TODO eventually the board should probably be split from the ruleset, which can manage points and
    // valid plays and such. The board should just manage state and maybe include utility methods that make
    // implementing rulesets easier.
    // TODO checks should just be raw correctness. For example, ensure word isn't placed of the board and won't run
    //  off the board should be checked, to prevent index out of bounds exception. Things like empty words, overrunning
    //  other words, etc. should NOT be checked. That is the responsibility of the ruleset
    public boolean isWordValid(String word, Direction direction, int row, int column) {
        // initial space is not on the board
        Point p = Point.of(row, column);
        if (!isInBounds(p)) {
            return false;
        }

        p.toEndOfWord(direction, word);
        if (!isInBounds(p)) {
            return false;
        }

        // intentionally split final check from the default "true" return to ease adding extra checks in the future.
        return true;
    }

    public boolean isInBounds(Point p) {
        return p.getRow() >= 0 && p.getRow() < size
                && p.getColumn() >= 0 && p.getColumn() < size;
    }

    // TODO this should probably return point value, or at least the list of letters that did not intersect???
    public void addWord(String word, Direction direction, int row, int column) {
        if (!isWordValid(word, direction, row, column)) {
            throw new IllegalArgumentException(
                    "Word " + word +
                            " at location (" + row + "," + column + ")" +
                            " in direction " + direction.asName() +
                            " is not valid on game board:\n" + toString()
            );
        }

        String asUpperCase = word.toUpperCase(Locale.ENGLISH);
        Point p = Point.of(row, column);
        for (Character c : asUpperCase.toCharArray()) {
            board[p.getRow()][p.getColumn()] = c;
            p.increment(direction);
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

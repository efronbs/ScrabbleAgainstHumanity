package com.efronbs.game;

// Optimized class representing a space on the scrabble board
// NOTE: this class is mutable and is INTENDED to be mutated, and unless explicitly stated in the javadocs, all state
// change methods mutate the Point. The point is returned from mutable methods to allow for ergonomic multistep
// mutations to a point. Should make usage efficient
// this class is likely going to be used in many hot loops, so low memory footprint and high performance is critical
// for this class.
final class Point {

    // TODO consider removing accssor methods and just exposing these fields
    //  This class is package private so it should be safe to ease constraints in favor of performance
    private int row;
    private int column;

    Point(int row, int column) {
        this.row = row;
        this.column = column;
    }

    // factory method for more ergonomic creation
    static Point of(int row, int column) {
        return new Point(row, column);
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    /**
     * Increments the point 1 space in the given direction.
     * @param direction the direction to increment
     */
    Point increment(Direction direction) {
        move(direction, 1);
        return this;
    }

    /**
     * Decrements the point 1 space in the given direction.
     * @param direction the direction to decrement
     */
    Point decrement(Direction direction) {
        move(direction, -1);
        return this;
    }

    Point toEndOfWord(Direction direction, String word) {
        if (word.isEmpty()) {
            return this;
        }
        move(direction, word.length() - 1);
        return this;
    }

    // Moves the point in the given direction the given number of spaces. Negative numbers are valid and move the point
    // in the opposite direction as positive numbers.
    // vertical words move down, horizontal words move right
    // 0,0 is located at top left of the board, so moving "down" rows actually increases row number
    Point move(Direction direction, int spaces) {
        if (direction.equals(Direction.VERTICAL)) {
            row += spaces;
        } else {
            column+= spaces;
        }
        return this;
    }

    /**
     * Given 2 points that make a valid horizontal or vertical line segment, is this point located on that line segment.
     * There is no required ordering for the parameters. As long as the create a valid line segment this method will
     * succeed.
     *
     * @param p1  Endpoint of line segment
     * @param p2  Endpoint of line segment
     * @return whether or not this point is on the line segment.
     * @throws IllegalArgumentException argument exception if p1 and p2 don't make a valid line segment
     */
    boolean isOnLine(Point p1, Point p2) {
        int valToCheck;
        int higherEndpoint;
        int lowerEndpoint;

        // line segment is vertical
        if (p1.getColumn() == p2.getColumn()) {
            // point is offset horizontally from vertical line segment
            if (column != p1.getColumn()) {
                return false;
            }
            valToCheck = row;
            higherEndpoint = Math.max(p1.getRow(), p2.getRow());
            lowerEndpoint = Math.min(p1.getRow(), p2.getRow());
        }
        // line segment is horizontal
        else if (p1.getRow() == p2.getRow()) {
            if (row != p1.getRow()) {
                return false;
            }
            valToCheck = column;
            higherEndpoint = Math.max(p1.getColumn(), p2.getColumn());
            lowerEndpoint = Math.min(p1.getColumn(), p2.getColumn());
        // invalid line segment
        } else {
            throw new IllegalArgumentException(
                    "Points (" + p1.getRow() + "," + p1.getColumn() + ")" +
                            " and (" + p2.getRow() + "," + p2.getColumn() + ")" +
                            " do not make a valid line segment"
            );
        }
        return valToCheck <= higherEndpoint && valToCheck >= lowerEndpoint;
    }

    /**
     * Returns a copy of this point.
     *
     * @return the copy
     */
    Point copy() {
        return Point.of(row, column);
    }
}

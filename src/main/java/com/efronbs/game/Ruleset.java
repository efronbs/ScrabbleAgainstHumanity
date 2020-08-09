package com.efronbs.game;

/**
 * Controller object.
 *
 * Implementations are assumed to be thread safe. Implementor must explicitly document if implementation is not thread
 * safe.
 */
public interface Ruleset {

    // TODO size can probably eventually be replaced with board config. This will allow finer grain specialty board
    //  creation hat is also validated by the ruleset
    /**
     * Generates a game board using the provided config. Implementing rulesets should document their board config
     * requirements.
     *
     * @param size
     * @return
     */
    GameBoard generateBoard(int size);

    // TODO add methods to this interface as they come up
    boolean isWordValid(
            GameBoard board,
            String word,
            Direction direciton,
            int row,
            int column
    );
}

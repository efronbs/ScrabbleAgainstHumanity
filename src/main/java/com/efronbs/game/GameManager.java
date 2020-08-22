package com.efronbs.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller that manages the state and lifecycles of a game.
 */
public class GameManager {

    private final Ruleset ruleset;
    private final GameBoard board;
    private final GameState gameState;

    /**
     * Constructor. Creates a manager to manage a single running game. The game must be started through the TBD method.
     *
     * @param ruleset
     * @param players
     */
    public GameManager(
            Ruleset ruleset,
            List<Player> players,
            int boardSize
    ) {
        this.ruleset = ruleset;
        this.board = ruleset.generateBoard(boardSize);
        this.gameState = new GameState(Collections.unmodifiableList(new ArrayList<>(players)));
    }

    // TODO should these getter methods be here? If they are required, should they be public?

    public Ruleset getRuleset() {
        return ruleset;
    }

    public GameBoard getBoard() {
        return board;
    }

    public GameState getGameState() {
        return gameState;
    }

    /**
     * Submit a word to the game manager. Increments the turn afterwards.
     * TODO maybe don't increment the turn?
     * TODO there should probably be more interaction than just true or false.
     *
     * @param word  The word submitted
     * @param p  Player submitting the word
     * @param startRow  Row containing first letter of the submitted word
     * @param startColumn  Column containing first letter of the submitted word
     * @param direction  Direction the word is going
     * @return whether or not the word was successfully submitted
     */
    public boolean submitWord(String word, Player p, Direction direction, int startRow, int startColumn) {
        if (!java.util.Objects.equals(gameState.getCurrentTurnPlayer(), p)) {
            return false;
        }

        if (!ruleset.isWordValid(board, word, direction, startRow, startColumn)) {
            return false;
        }

        board.addWord(word, direction, startRow, startColumn);
        gameState.incrementTurn();
        return true;
    }
}

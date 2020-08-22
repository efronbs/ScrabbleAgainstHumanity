package com.efronbs.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class that represents that state of a running game.
 */
public class GameState {

    private final List<Player> players;
    private int turn;

    /**
     * Constructor. Sets the initial state of the game. The initial list of players must be set to turn order.
     *
     * @param players
     */
    public GameState(List<Player> players) {
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.turn = 0;
    }

    /**
     * Returns players in turn order.
     *
     * @return
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the player object for the given name.
     * If no such player exists, null is returned
     *
     * @param name
     * @return
     */
    public Player playerForName(String name) {
        return players.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Player getCurrentTurnPlayer() {
        return players.get(turn);
    }

    /**
     * Sets the current turn to the provided player. Turn order is maintained.
     *
     * @param p
     * @throws IllegalArgumentException if the provided play is not part of this game.
     */
    public void setTurn(Player p) {
        turn = players.indexOf(p);
        if (turn < 0) {
            throw new IllegalArgumentException("Player " + p.getName() + " is not part of this game");
        }
    }

    public void incrementTurn() {
        turn = (turn + 1) % players.size();
    }
}

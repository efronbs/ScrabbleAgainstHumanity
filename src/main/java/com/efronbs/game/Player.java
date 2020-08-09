package com.efronbs.game;

/**
 * Model object that tracks state of a player. Players are always associated with a game.
 */
public class Player {
    // id is unique in a game but not unique over all games
    private int id;

    public Player(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

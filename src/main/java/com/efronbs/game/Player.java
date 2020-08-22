package com.efronbs.game;

import java.util.Objects;

/**
 * Model object that tracks state of a player. Players are always associated with a game.
 */
public class Player {

    // id is unique in a game but not unique over all games
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player)) {
            return false;
        }
        Player that = (Player) obj;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public String toString() {
        return "Player{name=" + getName() + "}";
    }
}

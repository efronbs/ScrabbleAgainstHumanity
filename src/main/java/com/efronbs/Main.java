package com.efronbs;

import com.efronbs.game.Direction;
import com.efronbs.game.GameBoard;
import com.efronbs.game.Ruleset;
import com.efronbs.game.SimpleRuleset;

public class Main {

    public static void main(String[] args) {
        Ruleset r = new SimpleRuleset();
        GameBoard b = r.generateBoard(7);
        b.addWord("Hello", Direction.HORIZONTAL, 3, 1);
        b.addWord("World", Direction.VERTICAL, 2, 5);

        System.out.println(b.toString());
    }
}

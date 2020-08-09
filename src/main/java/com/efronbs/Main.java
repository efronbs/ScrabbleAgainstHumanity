package com.efronbs;

import com.efronbs.model.Direction;
import com.efronbs.model.GameBoard;
import com.efronbs.model.Ruleset;
import com.efronbs.model.SimpleRuleset;

public class Main {

    public static void main(String[] args) {
        Ruleset r = new SimpleRuleset();
        GameBoard b = r.generateBoard(7);
        System.out.println(r.isWordValid(b, "Hello", Direction.HORIZONTAL, 3, 1));
        b.addWord("Hello", Direction.HORIZONTAL, 3, 1);
        b.addWord("World", Direction.VERTICAL, 2, 5);

        System.out.println(b.toString());
    }
}

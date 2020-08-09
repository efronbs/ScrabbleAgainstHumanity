package com.efronbs.game;

import java.util.Locale;

public enum Direction {
    VERTICAL,
    HORIZONTAL
    ;

    public String asName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}

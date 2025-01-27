package com.nodiumhosting.backrooms.level;

import com.nodiumhosting.backrooms.level.generator.LevelGenerators;
import lombok.Getter;

@Getter
public enum Levels {
    LEVEL0(new Level(0, LevelDimensionTypes.LEVEL0.getDimensionType(), LevelGenerators.LEVEL0.getGenerator()));

    private final Level level;

    Levels(Level level) {
        this.level = level;
    }

    public static void init() {
        //noop
    }
}

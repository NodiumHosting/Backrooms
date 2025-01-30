package com.nodiumhosting.backrooms.level;

import com.nodiumhosting.backrooms.level.generator.LevelGenerators;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.LightingChunk;

@Getter
public enum Levels {
    LEVEL0(new Level(0, LevelDimensionTypes.LEVEL0.getDimensionType(), LevelGenerators.LEVEL0.getGenerator(), LightingChunk::new, new Pos(0, 0.5, 0))),
    LEVEL1(new Level(1, LevelDimensionTypes.LEVEL1.getDimensionType(), LevelGenerators.LEVEL1.getGenerator(), LightingChunk::new, Pos.ZERO));

    private final Level level;

    Levels(Level level) {
        this.level = level;
    }

    public static void init() {
        //noop
    }
}

package com.nodiumhosting.backrooms.level.generator;

import lombok.Getter;
import net.minestom.server.instance.generator.Generator;

@Getter
public enum LevelGenerators {
    LEVEL0(new Level0Generator());

    private final Generator generator;

    LevelGenerators(Generator generator) {
        this.generator = generator;
    }
}

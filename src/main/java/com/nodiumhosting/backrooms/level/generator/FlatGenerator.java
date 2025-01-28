package com.nodiumhosting.backrooms.level.generator;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

public class FlatGenerator implements Generator {
    @Override
    public void generate(@NotNull GenerationUnit unit) {
        if ((unit.absoluteStart().chunkX() != 0 && unit.absoluteStart().chunkX() != -1) || (unit.absoluteStart().chunkZ() != 0 && unit.absoluteStart().chunkZ() != -1)) return;
        unit.modifier().fillHeight(0, 1, Block.BEDROCK);
        unit.modifier().fillHeight(1, 4, Block.DIRT);
        unit.modifier().fillHeight(4, 5, Block.GRASS_BLOCK);
    }
}

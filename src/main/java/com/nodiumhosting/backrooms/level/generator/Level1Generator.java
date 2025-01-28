package com.nodiumhosting.backrooms.level.generator;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Level1Generator implements Generator {
    private static final int WAREHOUSE_HEIGHT = 10;
    private static final DynamicRegistry.Key<Biome> BIOME_KEY = MinecraftServer.getBiomeRegistry()
            .register("level1", Biome.builder().build());

    private final Random random = new Random();

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        Point start = unit.absoluteStart();
        Point size = unit.size();

        unit.modifier().fillBiome(BIOME_KEY);

        unit.modifier().fillHeight(-1, 0, Block.BROWN_CONCRETE);
        GeneratorUtils.fillHeightRandom(unit, WAREHOUSE_HEIGHT - 1, WAREHOUSE_HEIGHT, WeightedBlockSet.fromBlockList(List.of(Block.CYAN_CONCRETE, Block.PURPLE_CONCRETE)));

    }
}
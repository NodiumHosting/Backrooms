package com.nodiumhosting.backrooms.level.generator;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.UnitModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GeneratorUtils {
    public static void fillHeightRandom(GenerationUnit unit, int i, int i1, @NotNull WeightedBlockSet blockSet) {
        if (blockSet.isEmpty()) return;

        Point start = unit.absoluteStart();
        Point end = unit.absoluteEnd();
        UnitModifier modifier = unit.modifier();
        Random random = new Random();

        for (int x = start.blockX(); x < end.blockX(); x++) {
            for (int y = i; y < i1; y++) {
                for (int z = start.blockZ(); z < end.blockZ(); z++) {
                    Pos pos = new Pos(x, y, z);
                    WeightedBlock weightedBlock = blockSet.random(random);
                    if (weightedBlock == null) continue;
                    Block block = weightedBlock.block;
                    modifier.setBlock(pos, block);
                }
            }
        }
    }
}

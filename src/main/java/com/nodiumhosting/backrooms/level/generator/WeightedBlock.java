package com.nodiumhosting.backrooms.level.generator;

import net.minestom.server.instance.block.Block;

public class WeightedBlock {
    public final Block block;
    public final int weight;

    public WeightedBlock(Block block, int weight) {
        this.block = block;
        this.weight = weight;
    }

    public WeightedBlock(Block block) {
        this.block = block;
        this.weight = 1;
    }
}

package com.nodiumhosting.backrooms.level.generator;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedBlockSet {
    private int total = 0;
    private List<WeightedBlock> weightedBlockList = new ArrayList<>();

    public WeightedBlockSet(List<WeightedBlock> weightedBlockList) {
        this.weightedBlockList = weightedBlockList;
        this.weightedBlockList.forEach((weightedBlock -> {
            total += weightedBlock.weight;
        }));
    }

    public WeightedBlockSet() {}

    public static WeightedBlockSet fromBlockList(List<Block> blockList) {
        return new WeightedBlockSet(blockList.stream().map(WeightedBlock::new).toList());
    }

    public WeightedBlockSet add(WeightedBlock weightedBlock) {
        this.weightedBlockList.add(weightedBlock);
        this.total += weightedBlock.weight;
        return this;
    }

    public boolean isEmpty() {
        return this.total == 0;
    }

    @Nullable
    public WeightedBlock random(Random random) {
        int index = random.nextInt(0, total);

        int runningTotal = 0;
        WeightedBlock block = null;
        for (WeightedBlock iteratedWeightedBlock : weightedBlockList) {
            if (index <= runningTotal) break;
            runningTotal += iteratedWeightedBlock.weight;
            block = iteratedWeightedBlock;
        }

        return block;
    }
}

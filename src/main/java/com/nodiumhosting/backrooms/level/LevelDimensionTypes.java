package com.nodiumhosting.backrooms.level;

import net.minestom.server.MinecraftServer;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;

public enum LevelDimensionTypes {
    LEVEL0("backrooms:level0", DimensionType.builder().ambientLight(0.05f).build()),
    LEVEL1("backrooms:level1", DimensionType.builder().ambientLight(1f).build());

    private final DynamicRegistry.@NotNull Key<DimensionType> dimensionType;

    LevelDimensionTypes(String id, DimensionType dimensionType) {
        this.dimensionType = MinecraftServer.getDimensionTypeRegistry().register(id, dimensionType);
    }

    public DynamicRegistry.@NotNull Key<DimensionType> getDimensionType() {
        return dimensionType;
    }
}

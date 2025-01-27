package com.nodiumhosting.backrooms.level;

import net.minestom.server.MinecraftServer;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;

public enum LevelDimensionTypes {
    LEVEL0(MinecraftServer.getDimensionTypeRegistry().register("backrooms:level0", DimensionType.builder().ambientLight(0.05f).build()));

    private final DynamicRegistry.@NotNull Key<DimensionType> dimensionType;

    LevelDimensionTypes(DynamicRegistry.@NotNull Key<DimensionType> dimensionType) {
        this.dimensionType = dimensionType;
    }

    public DynamicRegistry.@NotNull Key<DimensionType> getDimensionType() {
        return dimensionType;
    }
}

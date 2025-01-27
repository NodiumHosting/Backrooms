package com.nodiumhosting.backrooms;

import net.minestom.server.MinecraftServer;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;

public enum DimensionTypes {
    BACKROOMS(MinecraftServer.getDimensionTypeRegistry().register("backrooms:backrooms", DimensionType.builder().ambientLight(0.05f).build()));

    private final DynamicRegistry.@NotNull Key<DimensionType> dimensionType;

    DimensionTypes(DynamicRegistry.@NotNull Key<DimensionType> dimensionType) {
        this.dimensionType = dimensionType;
    }

    public DynamicRegistry.@NotNull Key<DimensionType> getDimensionType() {
        return dimensionType;
    }
}

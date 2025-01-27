package com.nodiumhosting.backrooms.level;

import com.nodiumhosting.backrooms.Backrooms;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.world.DimensionType;

public class Level {
    public final int ID;
    public final DynamicRegistry.Key<DimensionType> dimensionTypeKey;
    public final InstanceContainer instanceContainer;
    public final Generator generator;

    public Level(int ID, DynamicRegistry.Key<DimensionType> dimensionTypeKey, Generator generator) {
        this.ID = ID;
        this.dimensionTypeKey = dimensionTypeKey;
        this.generator = generator;

        InstanceContainer instanceContainer = Backrooms.getInstanceManager().createInstanceContainer(dimensionTypeKey);
        instanceContainer.setGenerator(generator);
        instanceContainer.setChunkSupplier(LightingChunk::new);
        this.instanceContainer = instanceContainer;
    }
}

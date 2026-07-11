package com.stonecolossus;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class StoneColossusModEntities {
    public static EntityType<StoneColossusEntity> STONE_COLOSSUS;

    public static void register() {
        STONE_COLOSSUS = Registry.register(
                Registries.ENTITY_TYPE,
                StoneColossusMod.id("stone_colossus"),
                FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, StoneColossusEntity::new)
                        .dimensions(EntityDimensions.fixed(1.6f, 3.6f))
                        .trackRangeBlocks(64)
                        .build()
        );

        FabricDefaultAttributeRegistry.register(STONE_COLOSSUS, StoneColossusEntity.createColossusAttributes());
    }
}
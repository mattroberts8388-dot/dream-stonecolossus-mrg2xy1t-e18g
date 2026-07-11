package com.stonecolossus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class StoneColossusMod implements ModInitializer {
    public static final String MOD_ID = "stonecolossus";

    public static final Item COLOSSUS_CORE = new Item(new FabricItemSettings().maxCount(64));
    public static final Item COLOSSUS_HAMMER = new ColossusHammerItem(new FabricItemSettings().maxCount(1).maxDamage(1200));

    public static final ItemGroup MAIN_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(COLOSSUS_CORE))
            .displayName(Text.translatable("itemGroup.stonecolossus.main"))
            .entries((context, entries) -> {
                entries.add(COLOSSUS_CORE);
                entries.add(COLOSSUS_HAMMER);
            })
            .build();

    @Override
    public void onInitialize() {
        StoneColossusModEntities.register();

        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "colossus_core"), COLOSSUS_CORE);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "colossus_hammer"), COLOSSUS_HAMMER);
        Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "main"), MAIN_GROUP);

        // Drop a Colossus Core when the Stone Colossus dies.
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {});
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
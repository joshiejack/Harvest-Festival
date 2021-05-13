package uk.joshiejack.penguinlib.data.custom;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public interface HasLoot {
    ResourceLocation getLootTable();

    default void register() {
        ResourceLocation lootTable = getLootTable();
        if (lootTable != null && !LootTableList.getAll().contains(lootTable)) {
            LootTableList.register(lootTable);
        }
    }
}

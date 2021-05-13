package uk.joshiejack.penguinlib.data.custom.loot_table;

import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("loot_table:standard")
public class CustomLootTableData extends AbstractCustomData<ResourceLocation, CustomLootTableData> {
    @Nonnull
    @Override
    public ResourceLocation build(ResourceLocation registryName, @Nonnull CustomLootTableData data, @Nullable CustomLootTableData... unused) {
        return LootTableList.register(registryName);
    }
}

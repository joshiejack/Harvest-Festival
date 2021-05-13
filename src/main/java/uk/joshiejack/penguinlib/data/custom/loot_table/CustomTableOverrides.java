package uk.joshiejack.penguinlib.data.custom.loot_table;

import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("loot_table:override")
public class CustomTableOverrides extends AbstractCustomData<ResourceLocation, CustomTableOverrides> {
    public String target;

    @Nonnull
    @Override
    public ResourceLocation build(ResourceLocation registryName, @Nonnull CustomTableOverrides data, @Nullable CustomTableOverrides... unused) {
        CustomLoader.LOOT_MERGES.get(new ResourceLocation(data.target)).add(registryName);
        return LootTableList.register(registryName);
    }
}

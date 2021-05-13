package uk.joshiejack.harvestcore.data.custom.item;

import uk.joshiejack.harvestcore.item.custom.ItemAchievement;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:achievement")
public class CustomItemAchievement extends AbstractCustomData.ItemOrBlock<ItemAchievement, CustomItemAchievement> {
    @Nonnull
    @Override
    public ItemAchievement build(ResourceLocation registryName, @Nonnull CustomItemAchievement main, @Nullable CustomItemAchievement... data) {
        return new ItemAchievement(registryName, main, data);
    }
}

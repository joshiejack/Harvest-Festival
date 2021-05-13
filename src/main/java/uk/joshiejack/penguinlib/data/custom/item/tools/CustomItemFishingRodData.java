package uk.joshiejack.penguinlib.data.custom.item.tools;

import uk.joshiejack.penguinlib.data.custom.item.CustomItemTieredTool;
import uk.joshiejack.penguinlib.item.custom.tools.ItemCustomFishingRod;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:fishing_rod")
public class CustomItemFishingRodData extends CustomItemTieredTool<ItemCustomFishingRod, CustomItemFishingRodData> {
    @Nonnull
    @Override
    public ItemCustomFishingRod build(ResourceLocation registryName, @Nonnull CustomItemFishingRodData main, @Nullable CustomItemFishingRodData... sub) {
        return new ItemCustomFishingRod(registryName, main);
    }
}

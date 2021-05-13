package uk.joshiejack.penguinlib.data.custom.item.tools;

import uk.joshiejack.penguinlib.data.custom.item.CustomItemTieredTool;
import uk.joshiejack.penguinlib.item.custom.tools.ItemCustomWateringCan;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:watering_can")
public class CustomItemWateringCanData extends CustomItemTieredTool<ItemCustomWateringCan, CustomItemWateringCanData> {
    public int width, depth;

    @Nonnull
    @Override
    public ItemCustomWateringCan build(ResourceLocation registryName, @Nonnull CustomItemWateringCanData main, @Nullable CustomItemWateringCanData... sub) {
        return new ItemCustomWateringCan(registryName, main);
    }
}

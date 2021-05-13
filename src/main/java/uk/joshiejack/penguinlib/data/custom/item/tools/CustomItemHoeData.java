package uk.joshiejack.penguinlib.data.custom.item.tools;

import uk.joshiejack.penguinlib.data.custom.item.CustomItemTieredTool;
import uk.joshiejack.penguinlib.item.custom.tools.ItemCustomHoe;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:hoe")
public class CustomItemHoeData extends CustomItemTieredTool<ItemCustomHoe, CustomItemHoeData> {
    public int width, depth;

    @Nonnull
    @Override
    public ItemCustomHoe build(ResourceLocation registryName, @Nonnull CustomItemHoeData main, @Nullable CustomItemHoeData... sub) {
        return new ItemCustomHoe(registryName, main);
    }
}

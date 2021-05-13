package uk.joshiejack.penguinlib.data.custom.item.tools;

import uk.joshiejack.penguinlib.data.custom.item.CustomItemTieredTool;
import uk.joshiejack.penguinlib.item.custom.tools.ItemCustomShovel;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:shovel")
public class CustomItemShovelData extends CustomItemTieredTool<ItemCustomShovel, CustomItemShovelData> {
    public int width;
    public int depth;

    @Nonnull
    @Override
    public ItemCustomShovel build(ResourceLocation registryName, @Nonnull CustomItemShovelData main, @Nullable CustomItemShovelData... sub) {
        return new ItemCustomShovel(registryName, main);
    }
}

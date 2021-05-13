package uk.joshiejack.penguinlib.data.custom.item.tools;

import uk.joshiejack.penguinlib.data.custom.item.CustomItemTieredTool;
import uk.joshiejack.penguinlib.item.custom.tools.ItemCustomHammer;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:hammer")
public class CustomItemHammerData extends CustomItemTieredTool<ItemCustomHammer, CustomItemHammerData> {
    public int area;
    public int widthAndHeight;
    public int depth;

    @Nonnull
    @Override
    public ItemCustomHammer build(ResourceLocation registryName, @Nonnull CustomItemHammerData main, @Nullable CustomItemHammerData... sub) {
        return new ItemCustomHammer(registryName, main);
    }
}

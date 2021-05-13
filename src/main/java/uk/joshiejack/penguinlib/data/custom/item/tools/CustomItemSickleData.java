package uk.joshiejack.penguinlib.data.custom.item.tools;

import uk.joshiejack.penguinlib.data.custom.item.CustomItemTieredTool;
import uk.joshiejack.penguinlib.item.custom.tools.ItemCustomSickle;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:sickle")
public class CustomItemSickleData extends CustomItemTieredTool<ItemCustomSickle, CustomItemSickleData> {
    public int area;

    @Nonnull
    @Override
    public ItemCustomSickle build(ResourceLocation registryName, @Nonnull CustomItemSickleData main, @Nullable CustomItemSickleData... sub) {
        return new ItemCustomSickle(registryName, main);
    }
}

package uk.joshiejack.penguinlib.data.custom.item.tools;

import uk.joshiejack.penguinlib.data.custom.item.CustomItemTieredTool;
import uk.joshiejack.penguinlib.item.custom.tools.ItemCustomLumberAxe;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:lumber_axe")
public class CustomItemLumberAxeData extends CustomItemTieredTool<ItemCustomLumberAxe, CustomItemLumberAxeData> {
    public int hits;
    public int area;

    @Nonnull
    @Override
    public ItemCustomLumberAxe build(ResourceLocation registryName, @Nonnull CustomItemLumberAxeData main, @Nullable CustomItemLumberAxeData... sub) {
        return new ItemCustomLumberAxe(registryName, main);
    }
}

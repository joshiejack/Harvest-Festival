package uk.joshiejack.penguinlib.data.custom.item.tools;

import uk.joshiejack.penguinlib.data.custom.item.CustomItemTieredTool;
import uk.joshiejack.penguinlib.item.custom.tools.ItemCustomSword;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:sword")
public class CustomItemSwordData extends CustomItemTieredTool<ItemCustomSword, CustomItemSwordData> {
    @Nonnull
    @Override
    public ItemCustomSword build(ResourceLocation registryName, @Nonnull CustomItemSwordData main, @Nullable CustomItemSwordData... sub) {
        return new ItemCustomSword(registryName, main);
    }
}

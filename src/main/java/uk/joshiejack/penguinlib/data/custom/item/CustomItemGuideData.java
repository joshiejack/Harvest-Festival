package uk.joshiejack.penguinlib.data.custom.item;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.data.custom.CustomIcon;
import uk.joshiejack.penguinlib.item.custom.ItemCustomGuide;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@PenguinLoader(value = "item:book")
public class CustomItemGuideData extends AbstractItemData<ItemCustomGuide, CustomItemGuideData> {
    public List<Tab> tabs;

    @Nonnull
    @Override
    public ItemCustomGuide build(ResourceLocation registryName, @Nonnull CustomItemGuideData main, @Nullable CustomItemGuideData... sub) {
        return new ItemCustomGuide(registryName, main);
    }

    public static class Tab {
        public String name;
        public CustomIcon icon;
        public List<String> sub_tabs;
    }
}

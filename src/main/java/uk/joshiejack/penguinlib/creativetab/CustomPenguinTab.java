package uk.joshiejack.penguinlib.creativetab;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class CustomPenguinTab extends PenguinTab {
    private final ResourceLocation[] items;
    private final List<Item> itemsList = Lists.newArrayList();

    public CustomPenguinTab(String modid, ResourceLocation[] items, CreateIcon icon) {
        super(modid, icon);
        this.items = items;
    }

    public void init() {
        if (items != null) {
            for (ResourceLocation i: items) {
                Item item = Item.REGISTRY.getObject(i);
                if (item != null) {
                    itemsList.add(item.setCreativeTab(this));
                    if (item instanceof ItemBlock) {
                        ((ItemBlock)item).getBlock().setCreativeTab(this);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
        if (!itemsList.isEmpty()) {
            for (Item item: itemsList) {
                item.getSubItems(this, list);
            }
        } else super.displayAllRelevantItems(list);
    }
}

package uk.joshiejack.horticulture.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.horticulture.tileentity.TileStump;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.item.base.ItemSingular;

import javax.annotation.Nonnull;
import java.util.Map;

public class ItemSpores extends ItemSingular {
    public ItemSpores() {
        super(new ResourceLocation(Horticulture.MODID, "spores"));
        setHasSubtypes(true);
        setMaxStackSize(64);
        setCreativeTab(Horticulture.TAB);
    }

    public static TileStump.MushroomData getDataFromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) return TileStump.MushroomData.NULL;
        else {
            assert stack.getTagCompound() != null;
            return TileStump.registry.getValue(new ItemStack(stack.getTagCompound()));
        }
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        if (stack.getTagCompound() != null) {
            ItemStack internal = new ItemStack(stack.getTagCompound());
            return internal.getDisplayName() + " Spores";
        } else return super.getItemStackDisplayName(stack);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (Map.Entry<Holder, TileStump.MushroomData> holder : TileStump.registry.getEntries()) {
                ItemStack stack = new ItemStack(this);
                ItemStack internal = holder.getKey().getStacks().get(0);
                stack.setTagCompound(internal.writeToNBT(new NBTTagCompound()));
                items.add(stack); //YES ADD ME
            }
        }
    }
}

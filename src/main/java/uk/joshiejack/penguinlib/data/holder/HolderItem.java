package uk.joshiejack.penguinlib.data.holder;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

@SuppressWarnings("unused")
@PenguinLoader
public class HolderItem extends Holder {
    public static final Holder EMPTY = new HolderItem(Items.AIR);
    private Item item;

    public HolderItem() { super("item");}
    public HolderItem(Item item) {
        super("item");
        this.item = item;
    }

    @Override
    public boolean isEmpty() {
        return item == Items.AIR;
    }

    @Override
    public Holder create(ItemStack stack) {
        return new HolderItem(stack.getItem());
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() == item;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Item", String.valueOf(ForgeRegistries.ITEMS.getKey(item)));
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("Item")));
    }

    @Override
    public String toString() {
        return StackHelper.getStringFromStack(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolderItem that = (HolderItem) o;
        return Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }
}

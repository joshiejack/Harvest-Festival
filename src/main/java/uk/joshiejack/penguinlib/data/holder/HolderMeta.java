package uk.joshiejack.penguinlib.data.holder;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

@SuppressWarnings("unused")
@PenguinLoader
public class HolderMeta extends Holder {
    private Item item;
    private int damage;

    public HolderMeta() { super("meta"); }
    public HolderMeta(NBTTagCompound tag) {
        super("meta");
        deserializeNBT(tag);
    }

    public HolderMeta(Item item, int damage) {
        super("meta");
        this.item = item;
        this.damage = damage;
    }

    public HolderMeta(ItemStack stack) {
        this(stack.getItem(), stack.getItemDamage());
    }

    @Override
    public Holder create(ItemStack stack) {
        return new HolderMeta(stack);
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() == item && stack.getItemDamage() == damage;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Item", String.valueOf(ForgeRegistries.ITEMS.getKey(item)));
        tag.setInteger("Damage", damage);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("Item")));
        damage = nbt.getInteger("Damage");
    }

    @Override
    public String toString() {
        return StackHelper.getStringFromStack(new ItemStack(item, 1, damage));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolderMeta that = (HolderMeta) o;
        return damage == that.damage && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, damage);
    }
}

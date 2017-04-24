package joshie.harvest.core.base.item;

import joshie.harvest.api.core.HFRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ItemHFRegistry<I extends ItemHFRegistry, E extends HFRegistry<E>> extends ItemHFBase<I> {
    private final Map<ResourceLocation, E> newRegistry;
    private final String nbt;

    public ItemHFRegistry(String nbt, Map<ResourceLocation, E> newRegistry, CreativeTabs tabs) {
        super(tabs);
        this.nbt = nbt;
        this.newRegistry = newRegistry;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {}

    public ItemStack getStackFromObject(E e) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString(nbt, e.getResource().toString());
        stack.setTagCompound(tag);
        return stack;
    }

    public E getObjectFromStack(ItemStack stack) {
        if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey(nbt)) {
            return getDefaultValue();
        } else return newRegistry.get(new ResourceLocation(stack.getTagCompound().getString(nbt)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        list.addAll(newRegistry.values().stream().map(this::getStackFromObject).collect(Collectors.toList()));
    }

    protected abstract E getDefaultValue();
}

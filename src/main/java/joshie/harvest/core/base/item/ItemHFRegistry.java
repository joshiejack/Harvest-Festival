package joshie.harvest.core.base.item;

import joshie.harvest.api.core.HFRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ItemHFRegistry<I extends ItemHFRegistry, E extends HFRegistry<E>> extends ItemHFBase<I> {
    //TODO: Remove in 0.7+
    private final IForgeRegistry<E> oldRegistry;
    private final Map<ResourceLocation, E> newRegistry;
    private final String nbt;

    public ItemHFRegistry(String nbt, IForgeRegistry<E> oldRegistry, Map<ResourceLocation, E> newRegistry, CreativeTabs tabs) {
        super(tabs);
        this.nbt = nbt;
        this.oldRegistry = oldRegistry; //TODO: Remove in 0.7+
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
        //TODO: Remove in 0.7+
        if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey(nbt)) {
            int id = Math.max(0, Math.min(oldRegistry.getValues().size() - 1, stack.getItemDamage()));
            NBTTagCompound tag = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
            E object = oldRegistry.getValues().get(id);
            if (object == null) object = getDefaultValue();
            tag.setString(nbt, object.getResource().toString());
            stack.setTagCompound(tag);
            return object;
        } else return newRegistry.get(new ResourceLocation(stack.getTagCompound().getString(nbt)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list) {
        list.addAll(newRegistry.values().stream().map(this::getStackFromObject).collect(Collectors.toList()));
    }

    protected abstract E getDefaultValue();
}

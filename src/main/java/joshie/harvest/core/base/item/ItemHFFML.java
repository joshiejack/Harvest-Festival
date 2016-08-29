package joshie.harvest.core.base.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemHFFML<I extends ItemHFFML, E extends Impl<E>> extends ItemHFBase<I> {
    protected IForgeRegistry<E> registry;
    public ItemHFFML(IForgeRegistry<E> registry) {
        super();
        this.registry = registry;
        setHasSubtypes(true);
    }

    public ItemHFFML(IForgeRegistry<E> registry, CreativeTabs tab) {
        super(tab);
        this.registry = registry;
        setHasSubtypes(true);
    }

    @Override
    public I setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        return (I) this;
    }

    public abstract E getNullValue();

    public E getObjectFromStack(ItemStack stack) {
        E e = registry.getValues().get(stack.getItemDamage());
        return e != null ? e: getNullValue();
    }

    public ItemStack getStackFromResource(ResourceLocation resource) {
        return new ItemStack(this, 1, registry.getValues().indexOf(registry.getValue(resource)));
    }

    public ItemStack getStackFromObject(E e) {
        return new ItemStack(this, 1, registry.getValues().indexOf(e));
    }

    public ItemStack getCreativeStack(Item item, E e) {
        return new ItemStack(item, 1, registry.getValues().indexOf(e));
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public boolean shouldDisplayInCreative(E e) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (E e: registry.getValues()) {
            if (shouldDisplayInCreative(e) && e != getNullValue()) {
                list.add(getCreativeStack(item, e));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {}
}
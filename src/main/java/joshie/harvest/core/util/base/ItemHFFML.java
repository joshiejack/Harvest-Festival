package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemHFFML<E extends Impl<E>> extends ItemHFBase {
    protected FMLControlledNamespacedRegistry<E> registry;
    public ItemHFFML(FMLControlledNamespacedRegistry<E> registry) {
        super();
        this.registry = registry;
        setHasSubtypes(true);
    }

    public ItemHFFML(FMLControlledNamespacedRegistry<E> registry, CreativeTabs tab) {
        super(tab);
        this.registry = registry;
        setHasSubtypes(true);
    }

    @Override
    public ItemHFFML setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        return this;
    }

    public abstract E getNullValue();

    public E getObjectFromStack(ItemStack stack) {
        E e = registry.getObjectById(stack.getItemDamage());
        return e != null ? e: getNullValue();
    }

    public ItemStack getStackFromResource(ResourceLocation resource) {
        return new ItemStack(this, 1, registry.getId(resource));
    }

    public ItemStack getStackFromObject(E e) {
        return new ItemStack(this, 1, registry.getIDForObject(e));
    }

    public ItemStack getCreativeStack(Item item, E e) {
        return new ItemStack(item, 1, registry.getIDForObject(e));
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public boolean isValidTab(CreativeTabs tab, E e) {
        return tab == getCreativeTab();
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{HFTab.FARMING, HFTab.COOKING, HFTab.MINING, HFTab.TOWN, HFTab.GATHERING};
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (E e: registry.getValues()) {
            if (isValidTab(tab, e)) {
                list.add(getCreativeStack(item, e));
            }
        }
    }
}
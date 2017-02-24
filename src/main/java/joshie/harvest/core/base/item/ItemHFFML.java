package joshie.harvest.core.base.item;

import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.core.util.interfaces.IFMLItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ItemHFFML<I extends ItemHFFML, E extends Impl<E>> extends ItemHFBase<I> implements ICreativeSorted, IFMLItem {
    protected final IForgeRegistry<E> registry;
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
    @SuppressWarnings("unchecked")
    public I setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        return (I) this;
    }

    @Override
    public abstract E getNullValue();

    @Override
    public E getObjectFromStack(ItemStack stack) {
        int id = Math.max(0, Math.min(registry.getValues().size() - 1, stack.getItemDamage()));
        E e = registry.getValues().size() == 0 ? null : registry.getValues().get(id);
        return e != null ? e: getNullValue();
    }

    @Override
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
    public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list) {
        list.addAll(registry.getValues().stream().filter(e -> shouldDisplayInCreative(e) && e != getNullValue()).map(e -> getCreativeStack(item, e)).collect(Collectors.toList()));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {}
}
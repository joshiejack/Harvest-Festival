package joshie.harvest.core.util.base;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemHFEnum<I extends ItemHFEnum, E extends Enum<E>> extends ItemHFBase implements ICreativeSorted {
    protected final E[] values;
    protected final String prefix;

    public ItemHFEnum(Class<E> clazz) {
        super();
        values = clazz.getEnumConstants();
        prefix = clazz.getSimpleName().toLowerCase();
        setHasSubtypes(true);
    }

    public ItemHFEnum(CreativeTabs tab, Class<E> clazz) {
        super(tab);
        values = clazz.getEnumConstants();
        prefix = clazz.getSimpleName().toLowerCase();
        setHasSubtypes(true);
    }

    @Override
    public I setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        return (I) this;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public boolean isValidTab(CreativeTabs tab, E e) {
        return tab == getCreativeTab();
    }

    public E getEnumFromStack(ItemStack stack) {
        if (stack.getItem() != this) return null;

        return getEnumFromMeta(stack.getItemDamage());
    }

    public E getEnumFromMeta(int meta) {
        if (meta < 0 || meta >= values.length) {
            meta = 0;
        }

        return values[meta];
    }

    public ItemStack getStackFromEnum(E e) {
        return new ItemStack(this, 1, e.ordinal());
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return prefix + "_" + getEnumFromStack(stack).name().toLowerCase();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.translate(getUnlocalizedName(stack).replaceAll("(.)([A-Z])", "$1$2").toLowerCase().replace("_", "."));
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{ HFTab.FARMING, HFTab.COOKING, HFTab.MINING, HFTab.TOWN, HFTab.GATHERING };
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.NONE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (E e: values) {
            if (isValidTab(tab, e)) {
                list.add(new ItemStack(item, 1, e.ordinal()));
            }
        }
    }

    protected String getPrefix(E e) {
        return e.getClass().getSimpleName().toLowerCase();
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, getPrefix(values[i]) + "_" + values[i].name().toLowerCase()), "inventory"));
        }
    }
}
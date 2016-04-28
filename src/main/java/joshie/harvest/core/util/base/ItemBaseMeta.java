package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.generic.IHasMetaItem;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemBaseMeta extends Item implements IHasMetaItem {
    protected String mod;
    protected String path;

    public ItemBaseMeta(String mod, CreativeTabs tab) {
        setCreativeTab(tab);
        setHasSubtypes(true);
        this.mod = mod;
    }

    public void setTextureFolder(String thePath) {
        this.path = thePath;
    }

    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        GameRegistry.registerItem(this, name.replace(".", "_"));
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return mod + "." + super.getUnlocalizedName().replace("item.", "");
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = getName(stack).replaceAll("(.)([A-Z])", "$1$2").toLowerCase();
        return Text.localize(getUnlocalizedName() + "." + name.replace("_", "."));
    }

    public String getName(ItemStack stack) {
        return "name";
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public boolean isValidTab(CreativeTabs tab, int meta) {
        return tab == getCreativeTab();
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{HFTab.FARMING, HFTab.COOKING, HFTab.MINING, HFTab.TOWN};
    }

    public boolean isActive(int damage) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < getMetaCount(); i++) {
            if (isActive(i) && isValidTab(tab, i)) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
}
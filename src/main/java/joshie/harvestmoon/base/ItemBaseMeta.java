package joshie.harvestmoon.base;

import java.util.List;

import joshie.harvestmoon.util.generic.IHasMetaItem;
import joshie.harvestmoon.util.generic.Text;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemBaseMeta extends Item implements IHasMetaItem {
    @SideOnly(Side.CLIENT)
    protected IIcon[] icons;
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

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        return icons[damage < icons.length ? damage : 0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        String path = this.path != null ? this.path : mod + ":";
        icons = new IIcon[getMetaCount()];
        for (int i = 0; i < icons.length; i++) {
            icons[i] = register.registerIcon(path + getName(new ItemStack(this, 1, i)));
        }
    }
    
    public boolean isValidTab(CreativeTabs tab, int meta) {
        return tab == getCreativeTab();
    }

    public boolean isActive(int damage) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < getMetaCount(); i++) {
            if (isActive(i) && isValidTab(tab, i)) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
}

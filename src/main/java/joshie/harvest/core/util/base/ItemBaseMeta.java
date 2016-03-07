package joshie.harvest.core.util.base;

import java.util.List;

import joshie.harvest.api.cooking.ICookingAltIcon;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.generic.IHasMetaItem;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemBaseMeta extends Item implements IHasMetaItem, ICookingAltIcon {
    @SideOnly(Side.CLIENT)
    protected IIcon[] icons;
    @SideOnly(Side.CLIENT)
    protected IIcon[] alts;
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
        damage = Math.max(0, Math.min(getMetaCount() - 1, damage));
        if (icons == null) return Items.arrow.getIconFromDamage(0);
        return icons[damage];
    }

    @Override
    public IIcon getCookingIcon(ItemStack stack, int pass) {
        int damage = Math.max(0, Math.min(getMetaCount() - 1, stack.getItemDamage()));
        if (icons == null) return Items.arrow.getIconFromDamage(0);
        if (alts[damage] != null) return alts[damage];
        return icons[damage];
    }

    public boolean hasAlt(ItemStack stack) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        String path = this.path != null ? this.path : mod + ":";
        alts = new IIcon[getMetaCount()];
        icons = new IIcon[getMetaCount()];
        for (int i = 0; i < icons.length; i++) {
            ItemStack stack = new ItemStack(this, 1, i);
            icons[i] = register.registerIcon(path + getName(stack));
            if (hasAlt(stack)) {
                alts[i] = register.registerIcon(path + getName(stack) + "_alt");
            }
        }
    }

    public boolean isValidTab(CreativeTabs tab, int meta) {
        return tab == getCreativeTab();
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[] { HFTab.tabFarming, HFTab.tabCooking, HFTab.tabMining, HFTab.tabTown };
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

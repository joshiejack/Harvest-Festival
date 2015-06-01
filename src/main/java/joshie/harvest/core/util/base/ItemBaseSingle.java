package joshie.harvest.core.util.base;

import joshie.harvest.core.util.generic.Text;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemBaseSingle extends Item {
    protected String mod;
    protected String path;

    public ItemBaseSingle(String mod, CreativeTabs tab) {
        setCreativeTab(tab);
        setMaxStackSize(1);
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
        return mod + "." + super.getUnlocalizedName().replace("item.", "").replace("_", ".");
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.localize(getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        String path = this.path != null ? this.path : mod + ":";
        String name = super.getUnlocalizedName().replace("item.", "").toLowerCase();
        itemIcon = iconRegister.registerIcon(path + Text.removeDecimals(name));
    }
}

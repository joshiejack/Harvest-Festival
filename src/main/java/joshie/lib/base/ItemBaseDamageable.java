package joshie.lib.base;

import joshie.lib.util.Text;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemBaseDamageable extends Item {
    protected String mod;
    protected String path;
    
    public ItemBaseDamageable(String mod, CreativeTabs tab, int dmg) {
        setCreativeTab(tab);
        setMaxStackSize(1);
        setMaxDamage(dmg);
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

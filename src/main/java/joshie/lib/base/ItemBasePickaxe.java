package joshie.lib.base;

import joshie.lib.util.Text;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemBasePickaxe extends ItemPickaxe {
    protected String mod;
    protected String path;

    public ItemBasePickaxe(String mod, CreativeTabs tab, ToolMaterial brick) {
        super(brick);
        setCreativeTab(tab);
        this.mod = mod;
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

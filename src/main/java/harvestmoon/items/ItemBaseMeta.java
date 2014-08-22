package harvestmoon.items;

import static harvestmoon.lib.ModInfo.MODPATH;
import harvestmoon.HarvestTab;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBaseMeta extends Item {
    @SideOnly(Side.CLIENT)
    protected IIcon[] icons;
    protected int meta;
    protected String path = MODPATH + ":";
    
    public ItemBaseMeta() {
        setCreativeTab(HarvestTab.hm);
        setHasSubtypes(true);
    }
    
    public void setMaxMetaDamage(int max) {
        this.meta = max;
    }
    
    public void setTextureFolder(String path) {
        this.path = path;
    }
    
    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        GameRegistry.registerItem(this, name);
        return this;
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = StringUtils.replace(getName(stack.getItemDamage()), "_", ".");
        return StatCollector.translateToLocal(MODPATH + "." + super.getUnlocalizedName().substring(5) + "." + name);
    }
    
    public String getName(int dmg) {
        return "name";
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {        
        return icons[damage < icons.length? damage: 0];
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[meta];
        for(int i = 0; i < icons.length; i++) {
            icons[i] = register.registerIcon(path + getName(i));
        } 
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)  {
        for(int i = 0; i < meta; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}

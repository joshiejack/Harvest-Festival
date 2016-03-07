package joshie.harvest.core.util.base;

import joshie.harvest.core.util.generic.IHasMetaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockBase extends Block {
    protected final String mod;
    protected BlockBase(Material material, String mod, CreativeTabs tab) {
        super(material);
        setCreativeTab(tab);
        this.mod = mod;
    }

    @Override
    public BlockBase setBlockName(String name) {
        super.setBlockName(name);
        String register = name.replace(".", "_");
        if(this instanceof IHasMetaBlock) {
            Class clazz = ((IHasMetaBlock) this).getItemClass();
            if(clazz == null) {
                String pack = this.getClass().getPackage().getName() + ".items.";
                String thiz = "Item" + this.getClass().getSimpleName();
                try {
                    clazz = (Class<? extends ItemBlock>) Class.forName(pack + thiz);
                } catch (Exception e) {}
            }
            
            GameRegistry.registerBlock(this, clazz, register);
        } else GameRegistry.registerBlock(this, register);
        
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return mod + "." + super.getUnlocalizedName();
    }
}

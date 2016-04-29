package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.IHasMetaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockHFBase extends Block {
    protected final String mod;

    //General Constructor
    public BlockHFBase(Material material, CreativeTabs tab) {
        super(material);
        setCreativeTab(tab);
        this.mod = HFModInfo.MODPATH;
    }

    //Default to farming constructor
    public BlockHFBase(Material material) {
        this(material, HFTab.FARMING);
    }

    @Override
    public BlockHFBase setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        String register = name.replace(".", "_");
        if (this instanceof IHasMetaBlock) {
            Class<? extends ItemBlock> clazz = ((IHasMetaBlock) this).getItemClass();
            if (clazz == null) {
                String pack = this.getClass().getPackage().getName() + ".items.";
                String thiz = "Item" + this.getClass().getSimpleName();
                try {
                    clazz = (Class<? extends ItemBlock>) Class.forName(pack + thiz);
                } catch (Exception ignored) {
                }
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
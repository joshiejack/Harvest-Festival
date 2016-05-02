package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.IHasMetaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockHFBase extends Block {

    //General Constructor
    public BlockHFBase(Material material, CreativeTabs tab) {
        super(material);
        setCreativeTab(tab);
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
        } else {
            GameRegistry.register(this, new ResourceLocation(HFModInfo.MODID, register));
            GameRegistry.register(new ItemBlock(this), new ResourceLocation(HFModInfo.MODID, register));
        }

        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return HFModInfo.MODID + "." + super.getUnlocalizedName();
    }
}
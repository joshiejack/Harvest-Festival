package joshie.harvest.core.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public abstract class ItemHFBase<I extends ItemHFBase> extends Item {
    public ItemHFBase() {
        this(HFTab.FARMING);
    }

    public ItemHFBase(CreativeTabs tab) {
        setCreativeTab(tab);
    }

    public I register(String name) {
        setUnlocalizedName(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameRegistry.register(this);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            registerModels(this, name);
        }

        return (I) this;
    }

    @Override
    public String getUnlocalizedName() {
        return HFModInfo.MODID + "." + super.getUnlocalizedName().replace("item.", "");
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        if (item.getHasSubtypes()) {
            List<ItemStack> subItems = new ArrayList<ItemStack>();
            if (item.getCreativeTabs().length > 0) {
                for (CreativeTabs tab : item.getCreativeTabs()) {
                    item.getSubItems(item, tab, subItems);
                }
            }

            for (ItemStack stack : subItems) {
                String subItemName = item.getUnlocalizedName(stack).replace("item.", "").replace(".", "_");

                ModelLoader.setCustomModelResourceLocation(item, item.getDamage(stack), new ModelResourceLocation(new ResourceLocation(MODID, subItemName), "inventory"));
            }
        } else {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(MODID, name), "inventory"));
        }
    }
}
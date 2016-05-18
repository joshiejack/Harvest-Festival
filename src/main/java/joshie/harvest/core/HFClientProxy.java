package joshie.harvest.core;

import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.ItemBlockHF;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class HFClientProxy extends HFCommonProxy {

    @Override
    public boolean isClient() {
        return true;
    }

    public void setBlockModelResourceLocation(Item item, String name) {
        name = name.replace(".", "_");
        if(item instanceof ItemBlockHF){
            ((ItemBlockHF) item).getBlock().registerModels(item, name);
        } else if(item != null){
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, name), "inventory"));
        }
    }
}

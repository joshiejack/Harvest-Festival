package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class HFClientProxy extends HFCommonProxy {

    @Override
    public boolean isClient() {
        return true;
    }

    public void setBlockModelResourceLocation(Item item, String name) {
        name = name.replace(".", "_");
        if (item != null) {
            if (item.getHasSubtypes()) {
                List<ItemStack> subBlocks = new ArrayList<ItemStack>();
                item.getSubItems(item, item.getCreativeTab(), subBlocks);
                for (ItemStack stack : subBlocks) {
                    String subBlockName = item.getUnlocalizedName(stack).replace(HFModInfo.MODID, "").replace(".", "_");

                    ModelLoader.setCustomModelResourceLocation(item, stack.getItemDamage(), new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, subBlockName), "inventory"));
                    HarvestFestival.LOGGER.log(Level.INFO, "SubBlockName: " + subBlockName);
                }
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, name), "inventory"));
                HarvestFestival.LOGGER.log(Level.INFO, "BlockName: " + name);
            }
        }
    }
}
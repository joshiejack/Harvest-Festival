package joshie.harvest.core.helpers.generic;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.Library;
import joshie.harvest.items.ItemBaseTool;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class RegistryHelper {

    public static Item registerItem(Item item, String name) {
        name = name.replace(".", "_");

        GameRegistry.register(item, new ResourceLocation(HFModInfo.MODID, name));

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            if (item.getHasSubtypes()) {
                List<ItemStack> subItems = new ArrayList<ItemStack>();
                item.getSubItems(item, item.getCreativeTab(), subItems);
                for (ItemStack stack : subItems) {
                    String subItemName = item.getUnlocalizedName(stack).replace("item.", "").replace(".", "_");

                    if (item instanceof ItemBaseTool) {
                        subItemName = subItemName + "_" + ((ItemBaseTool) item).getTier(stack).name().toLowerCase();
                    }

                    ModelLoader.setCustomModelResourceLocation(item, item.getDamage(stack), new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, subItemName), "inventory"));
                    HarvestFestival.logger.log(Level.INFO, "Sub Item Name " + subItemName);
                }
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, name), "inventory"));
                HarvestFestival.logger.log(Level.INFO, "Item Name " + name);
            }
        }

        if (Library.DEBUG_ON) {
            Library.log(Level.DEBUG, "Successfully registered the item " + item.getClass().getSimpleName() + " as " + HFModInfo.MODID + ":" + name);
        }

        return item;
    }

    //Short hand for registering tile entities
    public static void registerTiles(String mod, Class<? extends TileEntity>... tiles) {
        for (Class<? extends TileEntity> tile : tiles) {
            GameRegistry.registerTileEntity(tile, mod + ":" + tile.getSimpleName());
        }
    }

    private static void registerBlock(Block block) {
        String name = block.getUnlocalizedName().substring(5);
        name = name.replace('.', '_');
        if (Library.DEBUG_ON) {
            Library.log(Level.DEBUG, "Successfully registered the block " + block.getClass().getSimpleName() + " as " + HFModInfo.MODID + ":" + name);
        }

        GameRegistry.register(block, new ResourceLocation(HFModInfo.MODID, name));
        GameRegistry.register(new ItemBlock(block), new ResourceLocation(HFModInfo.MODID, name));
    }
}
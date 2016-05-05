package joshie.harvest.core.helpers.generic;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.IHasMetaBlock;
import joshie.harvest.core.util.generic.Library;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
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
                if (item.getCreativeTabs() != null && item.getCreativeTabs().length > 0) {
                    for (CreativeTabs tab : item.getCreativeTabs()) {
                        item.getSubItems(item, tab, subItems);
                    }
                }
                for (ItemStack stack : subItems) {
                    String subItemName = item.getUnlocalizedName(stack).replace("item.", "").replace(".", "_");

                    ModelLoader.setCustomModelResourceLocation(item, item.getDamage(stack), new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, subItemName), "inventory"));
                    HarvestFestival.LOGGER.log(Level.INFO, "Sub item name " + subItemName);
                }
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, name), "inventory"));
                HarvestFestival.LOGGER.log(Level.INFO, "Item Name " + name);
            }
        }

        if (Library.DEBUG_ON) {
            Library.log(Level.DEBUG, "Successfully registered the item " + item.getClass().getSimpleName() + " as " + HFModInfo.MODID + ":" + name);
        }

        return item;
    }

    public static Block registerBlock(Block block, String name) {
        ResourceLocation resource = new ResourceLocation(HFModInfo.MODID, name.replace(".", "_"));
        ItemBlock item = null;
        if (block instanceof IHasMetaBlock) {
            try {
                Class<? extends ItemBlock> clazz = ((IHasMetaBlock) block).getItemClass();
                if (clazz == null) {
                    String pack = block.getClass().getPackage().getName() + ".items.";
                    String thiz = "Item" + block.getClass().getSimpleName();
                    clazz = (Class<? extends ItemBlock>) Class.forName(pack + thiz);
                }
                item = clazz.getConstructor(Block.class).newInstance(block);
            } catch (Exception ignored) {
            }
        }

        //If we ended up with null, then fix it
        if (item == null) item = new ItemBlock(block);
        GameRegistry.register(block, resource);
        GameRegistry.register(item, resource);

        HarvestFestival.proxy.setBlockModelResourceLocation(Item.getItemFromBlock(block), name);

        if (Library.DEBUG_ON) {
            Library.log(Level.DEBUG, "Successfully registered the block " + block.getClass().getSimpleName() + " as " + HFModInfo.MODID + ":" + name);
        }

        return block;
    }

    //Short hand for registering tile entities
    public static void registerTiles(String mod, Class<? extends TileEntity>... tiles) {
        for (Class<? extends TileEntity> tile : tiles) {
            GameRegistry.registerTileEntity(tile, mod + ":" + tile.getSimpleName());
        }
    }
}
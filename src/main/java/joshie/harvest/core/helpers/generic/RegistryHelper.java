package joshie.harvest.core.helpers.generic;

import joshie.harvest.core.util.generic.Library;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.registry.GameRegistry;

public class RegistryHelper {
    //Shorthand for registering items
    @Deprecated
    public static void registerItems(Item... items) {
        for (Item item : items) {
            registerItem(item);
        }
    }
    
    private static void registerItem(Item item) {
        try {
            String name = item.getUnlocalizedName().substring(5);
            name = name.replace('.', '_');
            if (Library.DEBUG_ON) {
                Library.log(Level.DEBUG, "Successfully registered the item " + item.getClass().getSimpleName() + " as Mariculture:" + name);
            }
            
            GameRegistry.registerItem(item, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Short hand for registering tile entities
    public static void registerTiles(String mod, Class<? extends TileEntity>... tiles) {
        for (Class<? extends TileEntity> tile : tiles) {
            GameRegistry.registerTileEntity(tile, mod + ":" + tile.getSimpleName());
        }
    }

    private static void registerMetaBlock(Class<? extends ItemBlock> clazz, Block block) {
        try {
            String name = block.getUnlocalizedName().substring(5);
            if (clazz == null) {
                clazz = (Class<? extends ItemBlock>) Class.forName(block.getClass().getCanonicalName().toString() + "Item");
            }

            name = name.replace('.', '_');
            if (Library.DEBUG_ON) {
                Library.log(Level.DEBUG, "Successfully registered the block " + block.getClass().getSimpleName() + " with the item " + clazz.getSimpleName() + " as Mariculture:" + name);
            }
            
            GameRegistry.registerBlock(block, clazz, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void registerBlock(Block block) {
        String name = block.getUnlocalizedName().substring(5);
        name = name.replace('.', '_');
        if (Library.DEBUG_ON) {
            Library.log(Level.DEBUG, "Successfully registered the block " + block.getClass().getSimpleName() + " as Mariculture:" + name);
        }
        
        GameRegistry.registerBlock(block, name);
    }
}

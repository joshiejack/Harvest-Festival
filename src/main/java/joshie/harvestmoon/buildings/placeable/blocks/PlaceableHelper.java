package joshie.harvestmoon.buildings.placeable.blocks;

import java.util.HashMap;

import joshie.harvestmoon.buildings.placeable.entities.PlaceableEntity;
import joshie.harvestmoon.buildings.placeable.entities.PlaceableItemFrame;
import joshie.harvestmoon.buildings.placeable.entities.PlaceablePainting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class PlaceableHelper {
    public static HashMap<String, PlaceableEntity> entities = new HashMap();
    private static final HashMap<String, String> names = new HashMap();

    public static String getBestGuessName(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(stack.getItem());
            String name = Block.blockRegistry.getNameForObject(block).replace("minecraft:", "");
            String print = "Blocks." + name;
            if (names.containsKey(name)) {
                print = names.get(name);
            }

            return print;
        } else {
            String name = Item.itemRegistry.getNameForObject(stack.getItem()).replace("minecraft:", "");
            String print = "Items." + name;
            if (names.containsKey(name)) {
                print = names.get(name);
            }

            return print;
        }
    }

    public static String getPlaceableBlockString(Block block, int meta, int x, int y, int z) {
        String print = getBestGuessName(new ItemStack(block));
        if (block instanceof BlockStairs) {
            return "list.add(new PlaceableStairs(" + print + ", " + meta + ", " + x + ", " + y + ", " + z + "));";
        } else if (block instanceof BlockTorch) {
            return "list.add(new PlaceableTorches(" + print + ", " + meta + ", " + x + ", " + y + ", " + z + "));";
        }

        return "list.add(new PlaceableBlock(" + print + ", " + meta + ", " + x + ", " + y + ", " + z + "));";
    }

    public static String getPlaceableEntityString(Entity entity, int x, int y, int z) {
        return entities.get(entity.getClass().getSimpleName()).getStringFor(entity, x, y, z);
    }

    static {
        entities.put("EntityItemFrame", new PlaceableItemFrame());
        entities.put("EntityPainting", new PlaceablePainting());
    }
}

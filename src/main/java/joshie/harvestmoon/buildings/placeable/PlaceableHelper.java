package joshie.harvestmoon.buildings.placeable;

import java.util.HashMap;

import joshie.harvestmoon.buildings.placeable.entities.PlaceableEntity;
import joshie.harvestmoon.buildings.placeable.entities.PlaceableItemFrame;
import joshie.harvestmoon.buildings.placeable.entities.PlaceablePainting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLever;
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

    /** TODO:
         * Anvil
         * Item Frames
         * Painting
         * Signs
         * Trapdoors
         * Pumpkins
         * Doors
         * Vines
         * Gates
         * IFaceable
         * Jack o Lanterns
         * Lily Pads
         * Bed
         * Skulls
         * Pistons
         * Redstone Torch
         * Tripwire
         * Dispenser
         * Dropper
         */

    public static String getPrefixString(Block block) {
        if (block instanceof BlockStairs) {
            return "Stairs";
        } else if (block instanceof BlockTorch || block instanceof BlockLever || block instanceof BlockButton) {
            return "Torches";
        } else if (block instanceof BlockDoor) {
            return "Door";
        } else if (block instanceof BlockFenceGate) {
            return "Gate";
        } else if (block instanceof BlockAnvil) {
            return "Anvil";
        } else if (block instanceof BlockFurnace || block instanceof BlockLadder || block instanceof BlockEnderChest) {
            return "Furnace";
        } else return "Block";
    }

    public static String getPlaceableBlockString(Block block, int meta, int x, int y, int z) {
        String print = getBestGuessName(new ItemStack(block));
        return "list.add(new Placeable" + getPrefixString(block) + "(" + print + ", " + meta + ", " + x + ", " + y + ", " + z + "));";
    }

    public static String getPlaceableEntityString(Entity entity, int x, int y, int z) {
        return entities.get(entity.getClass().getSimpleName()).getStringFor(entity, x, y, z);
    }

    static {
        entities.put("EntityItemFrame", new PlaceableItemFrame());
        entities.put("EntityPainting", new PlaceablePainting());
    }
}

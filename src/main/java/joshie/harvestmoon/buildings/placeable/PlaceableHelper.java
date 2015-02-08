package joshie.harvestmoon.buildings.placeable;

import java.util.HashMap;

import joshie.harvestmoon.buildings.placeable.entities.PlaceableEntity;
import joshie.harvestmoon.buildings.placeable.entities.PlaceableItemFrame;
import joshie.harvestmoon.buildings.placeable.entities.PlaceablePainting;
import joshie.lib.util.IFaceable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockVine;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class PlaceableHelper {
    public static HashMap<String, PlaceableEntity> entities = new HashMap();
    private static final HashMap<String, String> names = new HashMap();

    public static String getBestGuessName(ItemStack stack) {
        if (stack.getItem() != null && stack.getItem() instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(stack.getItem());
            String name = Block.blockRegistry.getNameForObject(block).replace("minecraft:", "");
            String print = "Blocks." + name;
            if (names.containsKey(name)) {
                print = names.get(name);
            }

            return print;
        } else if (stack.getItem() != null) {
            String name = Item.itemRegistry.getNameForObject(stack.getItem()).replace("minecraft:", "");
            String print = "Items." + name;
            if (names.containsKey(name)) {
                print = names.get(name);
            }

            return print;
        } else return "//TODO: ITEM NAME";
    }

    /** TODO:
         * Item Frames
         * Painting
         * Signs
         * Lily Pads
         * Bed
         * Skulls
         * Pistons
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
        } else if (block instanceof BlockPumpkin) {
            return "Pumpkin";
        } else if (block instanceof BlockTrapDoor) {
            return "TrapDoor";
        } else if (block instanceof BlockVine) {
            return "Vine";
        } else return "Block";
    }

    public static String getPlaceableIFaceableString(IFaceable tile, Block block, int meta, int x, int y, int z) {
        return "list.add(new PlaceableIFaceable" + "(HMBlocks.tiles, " + meta + ", " + x + ", " + y + ", " + z + ", ForgeDirection." + tile.getFacing() + "));";
    }

    public static String getPlaceableBlockString(Block block, int meta, int x, int y, int z) {
        String print = getBestGuessName(new ItemStack(block));
        if (block == Blocks.wooden_door) print = "Blocks.wooden_door";
        if (block == Blocks.iron_door) print = "Blocks.iron_door";
        if (block == Blocks.standing_sign) print = "Blocks.standing_sign";
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

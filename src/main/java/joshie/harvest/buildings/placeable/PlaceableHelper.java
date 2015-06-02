package joshie.harvest.buildings.placeable;

import java.util.HashMap;

import joshie.harvest.HarvestFestival;
import joshie.harvest.buildings.placeable.entities.PlaceableEntity;
import joshie.harvest.buildings.placeable.entities.PlaceableItemFrame;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.buildings.placeable.entities.PlaceablePainting;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

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
        } else return "//ITEM NAME";
    }

    public static String getPrefixString(Block block) {
        if (block instanceof BlockStairs) {
            return "Stairs";
        } else if (block instanceof BlockTorch || block instanceof BlockLever) {
            return "Torches";
        } else if (block instanceof BlockButton) {
            return "Button";
        } else if (block instanceof BlockDoor) {
            return "Door";
        } else if (block instanceof BlockLilyPad) {
            return "Lilypad";
        } else if (block instanceof BlockFenceGate) {
            return "Gate";
        } else if (block instanceof BlockAnvil) {
            return "Anvil";
        } else if (block instanceof BlockFurnace || block instanceof BlockEnderChest) {
            return "Furnace";
        } else if (block instanceof BlockLadder) {
            return "Ladder";
        } else if (block instanceof BlockPumpkin) {
            return "Pumpkin";
        } else if (block instanceof BlockTrapDoor) {
            return "TrapDoor";
        } else if (block instanceof BlockVine) {
            return "Vine";
        } else if (block instanceof BlockLog) {
            return "Log";
        } else if (block instanceof BlockQuartz) {
            return "Pillar";
        } else if (block instanceof BlockDoublePlant) {
            return "DoublePlant";
        } else if (block instanceof BlockWeb) {
            return "Web";
        } else if (block instanceof BlockFlowerPot) {
            return "FlowerPot";
        } else if (block instanceof BlockMushroom) {
            return "Mushroom";
        } else if (block instanceof BlockFlower) {
            return "Flower";
        } else return "Block";
    }

    public static String getPlaceableIFaceableString(IFaceable tile, Block block, int meta, int x, int y, int z) {
        return "list.add(new PlaceableIFaceable" + "(" + HFModInfo.BLOCKSNAME + ".woodmachines, " + meta + ", " + x + ", " + y + ", " + z + ", ForgeDirection." + tile.getFacing() + "));";
    }

    public static String getFloorSignString(String[] sign, Block block, int meta, int x, int y, int z) {
        String text = "new String[] { \"" + sign[0] + "\", \"" + sign[1] + "\", \"" + sign[2] + "\", \"" + sign[3] + "\" } ";
        return "list.add(new PlaceableSignFloor" + "(Blocks.standing_sign, " + meta + ", " + x + ", " + y + ", " + z + ", " + text + "));";
    }

    public static String getWallSignString(String[] sign, Block block, int meta, int x, int y, int z) {
        String text = "new String[] { \"" + sign[0] + "\", \"" + sign[1] + "\", \"" + sign[2] + "\", \"" + sign[3] + "\" } ";
        return "list.add(new PlaceableSignWall" + "(Blocks.wall_sign, " + meta + ", " + x + ", " + y + ", " + z + ", " + text + "));";
    }

    public static String getPlaceableBlockString(Block block, int meta, int x, int y, int z) {
        String print = getBestGuessName(new ItemStack(block));
        if (block == Blocks.wooden_door) print = "Blocks.wooden_door";
        if (block == Blocks.iron_door) print = "Blocks.iron_door";
        if (block == Blocks.standing_sign) print = "Blocks.standing_sign";
        if (block == Blocks.air) print = "Blocks.air";
        if (block == Blocks.wall_sign) print = "Blocks.wall_sign";
        if (block == Blocks.flower_pot) print = "Blocks.flower_pot";
        if (block == Blocks.cauldron) print = "Blocks.cauldron";
        if (block == Blocks.reeds) print = "Blocks.reeds";
        if (block == Blocks.cake) print = "Blocks.cake";
        if (print.equals("//ITEM NAME")) HarvestFestival.logger.log(Level.INFO, block);

        return "list.add(new Placeable" + getPrefixString(block) + "(" + print + ", " + meta + ", " + x + ", " + y + ", " + z + "));";
    }

    public static String getPlaceableEntityString(Entity entity, int x, int y, int z) {
        return entities.get(entity.getClass().getSimpleName()).getStringFor(entity, x, y, z);
    }

    static {
        entities.put("EntityItemFrame", new PlaceableItemFrame());
        entities.put("EntityPainting", new PlaceablePainting());
        entities.put("EntityNPC", new PlaceableNPC());
        entities.put("EntityNPCBuilder", new PlaceableNPC());
        entities.put("EntityNPCMiner", new PlaceableNPC());
        entities.put("EntityNPCShopkeeper", new PlaceableNPC());
    }
}

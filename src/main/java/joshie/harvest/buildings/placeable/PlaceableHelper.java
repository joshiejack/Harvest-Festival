package joshie.harvest.buildings.placeable;

import joshie.harvest.HarvestFestival;
import joshie.harvest.buildings.placeable.entities.PlaceableEntity;
import joshie.harvest.buildings.placeable.entities.PlaceableItemFrame;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.buildings.placeable.entities.PlaceablePainting;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.Level;

import java.util.HashMap;

public class PlaceableHelper {
    public static HashMap<String, PlaceableEntity> entities = new HashMap<String, PlaceableEntity>();
    private static final HashMap<String, String> names = new HashMap<String, String>();

    public static String getBestGuessName(ItemStack stack) {
        if (stack.getItem() != null && stack.getItem() instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(stack.getItem());
            String name = Block.REGISTRY.getNameForObject(block).getResourcePath();
            String print = "Blocks." + name;
            if (names.containsKey(name)) {
                print = names.get(name);
            }

            return print;
        } else if (stack.getItem() != null) {
            String name = Item.REGISTRY.getNameForObject(stack.getItem()).getResourcePath();
            String print = "Items." + name;
            if (names.containsKey(name)) {
                print = names.get(name);
            }

            return print;
        } else return "//ITEM NAME";
    }

    private static String getPrefixString(Block block) {
        if (block instanceof BlockStairs) {
            return "Stairs";
        } else if (block instanceof BlockTorch) {
            return "Torches";
        } else if (block instanceof BlockLever) {
            return "Lever";
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
        } else if (block instanceof BlockTripWireHook) {
            return "Tripwire";
        } else return "Block";
    }

    public static String getPlaceableIFaceableString(IFaceable tile, IBlockState state, BlockPos pos) {
        return "list.add(new PlaceableIFaceable" + "(" + HFModInfo.BLOCKSNAME + ".woodmachines, " + state + ", " + pos + ", ForgeDirection." + tile.getFacing() + "));";
    }

    public static String getFloorSignString(ITextComponent[] sign, IBlockState state, BlockPos pos) {
        String text = "new String[] { \"" + sign[0] + "\", \"" + sign[1] + "\", \"" + sign[2] + "\", \"" + sign[3] + "\" } ";
        return "list.add(new PlaceableSignFloor" + "(Blocks.standing_sign, " + state + ", " + pos + ", " + text + "));";
    }

    public static String getWallSignString(ITextComponent[] sign, IBlockState state, BlockPos pos) {
        String text = "new String[] { \"" + sign[0] + "\", \"" + sign[1] + "\", \"" + sign[2] + "\", \"" + sign[3] + "\" } ";
        return "list.add(new PlaceableSignWall" + "(Blocks.WALL_SIGN, " + state + ", " + pos + ", " + text + "));";
    }

    public static String getPlaceableBlockString(IBlockState state, BlockPos pos) {
        Block block = state.getBlock();
        String print = getBestGuessName(new ItemStack(block));
        if (block == Blocks.OAK_DOOR) print = "Blocks.OAK_DOOR";
        if (block == Blocks.BIRCH_DOOR) print = "Blocks.BIRCH_DOOR";
        if (block == Blocks.SPRUCE_DOOR) print = "Blocks.spruce_door";
        if (block == Blocks.JUNGLE_DOOR) print = "Blocks.jungle_door";
        if (block == Blocks.ACACIA_DOOR) print = "Blocks.acacia_door";
        if (block == Blocks.DARK_OAK_DOOR) print = "Blocks.dark_oak_door";
        if (block == Blocks.IRON_DOOR) print = "Blocks.iron_door";
        if (block == Blocks.STANDING_SIGN) print = "Blocks.standing_sign";
        if (block == Blocks.AIR) print = "Blocks.AIR";
        if (block == Blocks.WALL_SIGN) print = "Blocks.WALL_SIGN";
        if (block == Blocks.FLOWER_POT) print = "Blocks.FLOWER_POT";
        if (block == Blocks.CAULDRON) print = "Blocks.CAULDRON";
        if (block == Blocks.REEDS) print = "Blocks.REEDS";
        if (block == Blocks.CAKE) print = "Blocks.CAKE";
        if (print.equals("//ITEM NAME")) HarvestFestival.logger.log(Level.INFO, block);

        return "list.add(new Placeable" + getPrefixString(block) + "(" + print + ", " + state + ", " + pos + "));";
    }

    public static String getPlaceableEntityString(Entity entity, BlockPos pos) {
        return entities.get(entity.getClass().getSimpleName()).getStringFor(entity, pos);
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
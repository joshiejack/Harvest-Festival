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
            String print = "Blocks." + name.toUpperCase();
            if (names.containsKey(name)) {
                print = names.get(name);
            }

            return print;
        } else if (stack.getItem() != null) {
            String name = Item.REGISTRY.getNameForObject(stack.getItem()).getResourcePath();
            String print = "Items." + name.toUpperCase();
            if (names.containsKey(name)) {
                print = names.get(name);
            }

            return print;
        } else return "//ITEM NAME";
    }

    private static String getPrefixString(Block block) {
        if (block instanceof BlockTorch) {
            return "Decorative";
        } else if (block instanceof BlockLever) {
            return "Decorative";
        } else if (block instanceof BlockButton) {
            return "Decorative";
        } else if (block instanceof BlockDoor) {
            return "Decorative";
        } else if (block instanceof BlockLilyPad) {
            return "Decorative";
        } else if (block instanceof BlockCocoa) {
            return "Decorative";
        } else if (block instanceof BlockAnvil) {
            return "Decorative";
        } else if (block instanceof BlockFurnace || block instanceof BlockEnderChest) {
            return "Furnace";
        } else if (block instanceof BlockLadder) {
            return "Decorative";
        } else if (block instanceof BlockPumpkin) {
            return "Decorative";
        } else if (block instanceof BlockTrapDoor) {
            return "Decorative";
        } else if (block instanceof BlockVine) {
            return "Decorative";
        }  else if (block instanceof BlockDoublePlant) {
            return "DoublePlant";
        } else if (block instanceof BlockWeb) {
            return "Web";
        } else if (block instanceof BlockFlowerPot) {
            return "FlowerPot";
        } else if (block instanceof BlockMushroom) {
            return "Decorative";
        } else if (block instanceof BlockFlower) {
            return "Decorative";
        } else if (block instanceof BlockTripWireHook) {
            return "Decorative";
        } else return "Block";
    }

    public static String getPlaceableIFaceableString(IFaceable tile, IBlockState state, BlockPos pos) {
        return "list.add(new PlaceableIFaceable" + "(" + HFModInfo.BLOCKSNAME + ".woodmachines, " + state + ", " + pos + ", ForgeDirection." + tile.getFacing() + "));";
    }

    public static String getFloorSignString(ITextComponent[] sign, IBlockState state, BlockPos pos) {
        String text = "new String[] { \"" + sign[0] + "\", \"" + sign[1] + "\", \"" + sign[2] + "\", \"" + sign[3] + "\" } ";
        return "list.add(new PlaceableSignFloor" + "(Blocks.STANDING_SIGN, " + state + ", " + pos + ", " + text + "));";
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
        if (block == Blocks.SPRUCE_DOOR) print = "Blocks.SPRUCE_DOOR";
        if (block == Blocks.JUNGLE_DOOR) print = "Blocks.JUNGLE_DOOR";
        if (block == Blocks.ACACIA_DOOR) print = "Blocks.ACACIA_DOOR";
        if (block == Blocks.DARK_OAK_DOOR) print = "Blocks.DARK_OAK_DOOR";
        if (block == Blocks.IRON_DOOR) print = "Blocks.IRON_DOOR";
        if (block == Blocks.STANDING_SIGN) print = "Blocks.STANDING_SIGN";
        if (block == Blocks.AIR) print = "Blocks.AIR";
        if (block == Blocks.WALL_SIGN) print = "Blocks.WALL_SIGN";
        if (block == Blocks.FLOWER_POT) print = "Blocks.FLOWER_POT";
        if (block == Blocks.CAULDRON) print = "Blocks.CAULDRON";
        if (block == Blocks.REEDS) print = "Blocks.REEDS";
        if (block == Blocks.CAKE) print = "Blocks.CAKE";
        if (block == Blocks.DOUBLE_WOODEN_SLAB) print = "Blocks.DOUBLE_WOODEN_SLAB";
        if (block == Blocks.DOUBLE_STONE_SLAB) print = "Blocks.DOUBLE_STONE_SLAB";
        if (block == Blocks.DOUBLE_STONE_SLAB2) print = "Blocks.DOUBLE_STONE_SLAB2";
        if (block == Blocks.PURPUR_DOUBLE_SLAB) print = "Blocks.PURPUR_DOUBLE_SLAB";
        if (print.equals("//ITEM NAME")) HarvestFestival.LOGGER.log(Level.INFO, block);

        return "list.add(new Placeable" + getPrefixString(block) + "(" + print + ", " + "" + state.getBlock().getMetaFromState(state) + ", " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "));";
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
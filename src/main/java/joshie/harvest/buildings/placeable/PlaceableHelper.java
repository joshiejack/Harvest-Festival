package joshie.harvest.buildings.placeable;

import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.placeable.blocks.*;
import joshie.harvest.buildings.placeable.entities.PlaceableEntity;
import joshie.harvest.buildings.placeable.entities.PlaceableItemFrame;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.buildings.placeable.entities.PlaceablePainting;
import joshie.harvest.core.block.BlockStand;
import joshie.harvest.core.tile.TileStand;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.HashMap;

public class PlaceableHelper {
    public static final HashMap<String, PlaceableEntity> entities = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    private static Placeable getPrefixString(World world, IBlockState state, BlockPos actual, int x, int y, int z) {
        Block block = state.getBlock();
        if (block instanceof BlockStand) {
            return new PlaceableStand(((TileStand)world.getTileEntity(actual)).getContents(), state, x, y, z);
        } else  if (block instanceof joshie.harvest.mining.block.BlockPortal) {
            return new PlaceableMoveIn(state, x, y, z);
        } else if (block == HFBuildings.AIR || block == Blocks.GOLD_BLOCK) {
            return new PlaceableBlock(Blocks.AIR.getDefaultState(), x, y, z);
        } else if (block instanceof BlockTorch) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockLever || block  == Blocks.WATER) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockButton) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockDoor || block instanceof BlockDoublePlant) {
            IBlockState above = world.getBlockState(new BlockPos(x, y + 1, z));
            if (above.getBlock().getMetaFromState(above) == 9) return new PlaceableDoubleOpposite(state, x, y, z);
            else return new PlaceableDouble(state, x, y, z);
        } else if (block instanceof BlockLilyPad) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockCocoa) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockAnvil) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockLadder) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockPumpkin) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockTrapDoor) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockVine) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockWeb) {
            return new PlaceableWeb(state, x, y, z);
        } else if (block instanceof BlockFlowerPot) {
            return new PlaceableFlowerPot(state, x, y, z);
        } else if (block instanceof BlockMushroom) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockFlower) {
            return new PlaceableDecorative(state, x, y, z);
        } else if (block instanceof BlockTripWireHook) {
            return new PlaceableDecorative(state, x, y, z);
        } else return new PlaceableBlock(state, x, y, z);
    }

    public static PlaceableSign getFloorSignString(ITextComponent[] sign, IBlockState state, BlockPos pos) {
        return new PlaceableSign(state, pos.getX(), pos.getY(), pos.getZ(), sign);
    }

    public static PlaceableSign getWallSignString(ITextComponent[] sign, IBlockState state, BlockPos pos) {
        return new PlaceableSign(state, pos.getX(), pos.getY(), pos.getZ(), sign);
    }

    public static Placeable getPlaceableBlockString(World world, IBlockState state, BlockPos actual, int x, int y, int z) {
        Block block = state.getBlock();
        if (block == Blocks.REEDS) return new PlaceableDecorative(state, x, y, z);
        if (block == Blocks.CAKE) return new PlaceableDecorative(state, x, y, z);
        return getPrefixString(world, state, actual, x, y, z);
    }

    public static Placeable getPlaceableEntityString(Entity entity, int x, int y, int z) {
        return entities.get(entity.getClass().getSimpleName()).getCopyFromEntity(entity, x, y, z);
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
package uk.joshiejack.penguinlib.template;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.template.blocks.*;
import uk.joshiejack.penguinlib.template.entities.*;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

import java.util.HashMap;

public class PlaceableHelper {
    public static final HashMap<String, PlaceableEntity> entities = new HashMap<>();
    public static final Template.Replaceable DEFAULT = new Template.Replaceable();
    public static final BiMap<String, String> TYPE_REGISTRY = HashBiMap.create();

    @SuppressWarnings("ConstantConditions")
    private static Placeable getPrefixString(World world, IBlockState state, BlockPos offset) {
        Block block = state.getBlock();
        if (block == Blocks.BARRIER || block == PenguinLib.AIR) {
            return new PlaceableBlock(Blocks.AIR.getDefaultState(), offset);
        } else if (block instanceof BlockDoor || block instanceof BlockDoublePlant) {
            IBlockState above = world.getBlockState(offset.up());
            if (above.getBlock().getMetaFromState(above) == 9) return new PlaceableDoubleOpposite(state, offset);
            else return new PlaceableDouble(state, offset);
        } else if (block instanceof BlockLilyPad || block instanceof BlockCocoa || block instanceof BlockAnvil || block instanceof BlockLadder || block instanceof BlockPumpkin ||
                block instanceof BlockTrapDoor || block instanceof BlockVine || block instanceof BlockMushroom || block instanceof BlockFlower || block instanceof BlockTripWireHook ||
                block instanceof BlockButton || block instanceof BlockLever || block instanceof BlockFluidBase || block instanceof BlockLiquid || block instanceof BlockTorch) {
            return new PlaceableDecorative(state, offset);
        } else if (block instanceof BlockWeb) {
            return new PlaceableWeb(state, offset);
        } else if (block instanceof BlockFlowerPot) {
            return new PlaceableFlowerPot(state, offset);
        } else return new PlaceableBlock(state, offset);
    }

    public static PlaceableSign getFloorSignString(ITextComponent[] sign, IBlockState state, BlockPos pos) {
        return new PlaceableSign(state, pos, sign);
    }

    public static PlaceableSign getWallSignString(ITextComponent[] sign, IBlockState state, BlockPos pos) {
        return new PlaceableSign(state, pos, sign);
    }

    public static Placeable getPlaceableBlockString(World world, IBlockState state, BlockPos offset) {
        Block block = state.getBlock();
        if (block == Blocks.REEDS) return new PlaceableDecorative(state, offset);
        if (block == Blocks.CAKE) return new PlaceableDecorative(state, offset);
        return getPrefixString(world, state, offset);
    }

    public static Placeable getPlaceableEntityString(Entity entity, BlockPos position) {
        if (entity instanceof EntityLivingBase) return new PlaceableLiving().getCopyFromEntity(entity, position);
        //if (entity instanceof EntityItemFrame) return new
        return entities.get(entity.getClass().getSimpleName()).getCopyFromEntity(entity, position);
    }

    static {
        //TODO: Add placeable banner! and placeable cookware!
        entities.put("EntityItemFrame", new PlaceableItemFrame());
        entities.put("EntityPainting", new PlaceablePainting());
        entities.put("EntityArmorStand", new PlaceableArmorStand());
    }
}
package uk.joshiejack.penguinlib.block.base;

import uk.joshiejack.penguinlib.block.interfaces.IInteractable;
import uk.joshiejack.penguinlib.tile.inventory.TileInventory;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockMultiTile<E extends Enum<E> & IStringSerializable> extends BlockMulti<E> {
    public BlockMultiTile(ResourceLocation registry, Material material, Class<E> clazz) {
        super(registry, material, clazz);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof IInteractable && ((IInteractable) tile).onRightClicked(player, hand);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileInventory) {
            ((TileInventory)tile).updateTick();
        }
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileInventory) {
            ((TileInventory)tile).dropInventory();
            world.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(world, pos, state);
    }
}

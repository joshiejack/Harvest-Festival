package uk.joshiejack.penguinlib.block.custom;

import uk.joshiejack.penguinlib.block.interfaces.IInteractable;
import uk.joshiejack.penguinlib.data.custom.block.AbstractCustomBlockData;
import uk.joshiejack.penguinlib.data.custom.block.CustomBlockInventoryData;
import uk.joshiejack.penguinlib.tile.inventory.TileInventory;
import uk.joshiejack.penguinlib.tile.custom.AbstractTileCustomInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockMultiCustomInventory extends BlockMultiCustom {
    public BlockMultiCustomInventory(ResourceLocation registry, CustomBlockInventoryData defaults, CustomBlockInventoryData... data) {
        super(registry, defaults, data);
    }

    public ResourceLocation getScript(IBlockState state) {
        AbstractCustomBlockData data = getDataFromState(state);
        return data.getScript() == null ? getDefaults().getScript() : data.getScript();
    }

    public int getSize(int meta) {
        CustomBlockInventoryData data = (CustomBlockInventoryData) ids.inverse().get(meta);
        return data.inventorySize == -1 ? ((CustomBlockInventoryData)getDefaults()).inventorySize: data.inventorySize;
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

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new AbstractTileCustomInventory().withData(state, this);
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

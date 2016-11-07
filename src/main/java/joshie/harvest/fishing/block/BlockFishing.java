package joshie.harvest.fishing.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.base.tile.TileSingleStack;
import joshie.harvest.fishing.block.BlockFishing.FishingBlock;
import joshie.harvest.fishing.tile.TileHatchery;
import joshie.harvest.fishing.tile.TileTrap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

import static joshie.harvest.fishing.HFFishing.NO_WATER;
import static net.minecraft.block.BlockLiquid.LEVEL;

public class BlockFishing extends BlockHFEnum<BlockFishing, FishingBlock> {
    public BlockFishing() {
        super(Material.PISTON, FishingBlock.class, HFTab.FISHING);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, LEVEL, temporary);
        return new BlockStateContainer(this, LEVEL, property);
    }

    @SideOnly(Side.CLIENT)
    public boolean canRenderInLayer(BlockRenderLayer layer)  {
        return layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSingleStack) {
            return ((TileSingleStack)tile).onRightClicked(player, held);
        }

        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        switch (getEnumFromState(state)) {
            case TRAP:      return new TileTrap();
            case HATCHERY:  return new TileHatchery();
            default:        return null;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        ModelLoader.setCustomStateMapper(this, NO_WATER);
        super.registerModels(item, name);
    }

    public enum FishingBlock implements IStringSerializable {
        TRAP, HATCHERY;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

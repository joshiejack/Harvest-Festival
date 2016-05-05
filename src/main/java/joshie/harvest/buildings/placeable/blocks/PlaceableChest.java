package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableChest extends PlaceableBlock {
    private ResourceLocation chestType;

    public PlaceableChest(Block block, int meta, int x, int y, int z, ResourceLocation chestType) {
        super(block, meta, x, y, z);
        this.chestType = chestType;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(UUID uuid, World world, BlockPos pos, Direction direction) {
        TileEntity tile = world.getTileEntity(pos);
        if (chestType != null && tile instanceof TileEntityChest) {
            ((TileEntityChest)tile).setLoot(chestType, world.rand.nextLong());
        }
    }
}
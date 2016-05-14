package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableChest extends PlaceableBlock {
    private ResourceLocation chestType;

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Direction direction) {
        TileEntity tile = world.getTileEntity(pos);
        if (chestType != null && tile instanceof TileEntityChest) {
            ((TileEntityChest)tile).setLoot(chestType, world.rand.nextLong());
        }
    }
}
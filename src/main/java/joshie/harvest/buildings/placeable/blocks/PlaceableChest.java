package joshie.harvest.buildings.placeable.blocks;

import com.google.gson.annotations.Expose;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class PlaceableChest extends PlaceableBlock {
    @Expose
    private ResourceLocation chestType;

    public PlaceableChest() {}
    public PlaceableChest(String name, IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
        this.chestType = new ResourceLocation(MODID, name);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (chestType != null && tile instanceof TileEntityChest) {
            ((TileEntityChest)tile).setLootTable(chestType, world.rand.nextLong());
        }
    }
}
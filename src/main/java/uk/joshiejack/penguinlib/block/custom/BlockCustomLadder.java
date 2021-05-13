package uk.joshiejack.penguinlib.block.custom;

import uk.joshiejack.penguinlib.block.base.BlockLadderBase;
import uk.joshiejack.penguinlib.block.base.BlockMultiRotatable;
import uk.joshiejack.penguinlib.data.custom.block.AbstractCustomBlockData;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockCustomLadder extends BlockMultiCustomRotatable {
    public BlockCustomLadder(ResourceLocation registry, AbstractCustomBlockData defaults, AbstractCustomBlockData... data) {
        super(registry, defaults, data);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(BlockMultiRotatable.FACING)) {
            case NORTH:
                return BlockLadderBase.LADDER_NORTH_AABB;
            case SOUTH:
                return BlockLadderBase.LADDER_SOUTH_AABB;
            case WEST:
                return BlockLadderBase.LADDER_WEST_AABB;
            case EAST:
            default:
                return BlockLadderBase.LADDER_EAST_AABB;
        }
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        AbstractCustomBlockData data = getDataFromState(state);
        ResourceLocation script = data.script != null ? data.getScript() : getDefaults().getScript();
        if (script != null) {
            Scripting.callFunction(script, "onLadder", entity.world, entity, pos);
        }

        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    protected void registerModel(Item item, AbstractCustomBlockData cd) {
        if (StringHelper.isEarlierThan(property.getName(), 'f')) ModelLoader.setCustomModelResourceLocation(item, ids.get(cd), new ModelResourceLocation(getRegistryName(), property.getName() + "=" + cd.name + ",facing=north"));
        else ModelLoader.setCustomModelResourceLocation(item, ids.get(cd), new ModelResourceLocation(getRegistryName(), "facing=north," + property.getName() + "=" + cd.name));

    }
}

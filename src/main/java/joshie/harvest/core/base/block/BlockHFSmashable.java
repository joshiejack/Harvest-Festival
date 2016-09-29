package joshie.harvest.core.base.block;

import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.base.item.ItemToolSmashing;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockHFSmashable<B extends BlockHFSmashable, E extends Enum<E> & IStringSerializable> extends BlockHFEnum<B, E> implements ISmashable {
    //Main Constructor
    public BlockHFSmashable(Material material, Class<E> clazz, CreativeTabs tab) {
        super(material, clazz, tab);
    }

    public abstract ItemToolSmashing getTool();

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
        if (entityIn instanceof EntityPlayer || entityIn instanceof EntityItem) super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn);
        else addCollisionBoxToList(pos, entityBox, collidingBoxes, HFCore.FENCE_COLLISION);
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        ItemToolSmashing tool = getTool();
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == tool) {
            if (player.motionY <= -0.1F) {
                tool.smashBlock(world, player, pos, player.getHeldItemMainhand(), true);
            }
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)  {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots)  {
            EntityPlayer player = harvesters.get();
            if (player != null && player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == getTool()) {
                if (smashBlock(harvesters.get(), worldIn, pos, state, getTool().getTier(player.getHeldItemMainhand()))) return;
            }

            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }

    @Override
    protected int getToolLevel(E e) {
        switch (e.ordinal()) {
            case 0:
            case 1:
            case 2:
                return 1;
            case 3:
            case 4:
                return 2;
            case 5:
            case 6:
            case 7:
                return 3;
            default:
                return 0;
        }
    }
}
package joshie.harvest.core.base.block;

import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.core.base.item.ItemToolSmashing;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockHFSmashable<B extends BlockHFSmashable, E extends Enum<E> & IStringSerializable> extends BlockHFEnum<B, E> implements ISmashable {
    //Main Constructor
    public BlockHFSmashable(Material material, Class<E> clazz, CreativeTabs tab) {
        super(material, clazz, tab);
    }

    public abstract ItemToolSmashing getTool();

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
        if (isDroppable(getEnumFromState(state))) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }

    protected boolean isDroppable(E e) {
        return false;
    }

    @Override
    protected int getToolLevel(E e) {
        switch (e.ordinal()) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 3:
                return 3;
            case 2:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            default:
                return 0;
        }
    }
}
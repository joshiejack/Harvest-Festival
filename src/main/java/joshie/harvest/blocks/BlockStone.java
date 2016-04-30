package joshie.harvest.blocks;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockStone extends BlockHFBaseMeta<BlockStone.Types> {
    public enum Types implements IStringSerializable {
        REAL, DECORATIVE;

        @Override
        public String getName() {
            return toString();
        }
    }

    public BlockStone() {
        super(Material.ROCK, HFTab.MINING, Types.class);
        setSoundType(SoundType.METAL);
    }

    //TECHNICAL
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        switch (getEnumFromState(state)) {
            case REAL:
                return -1.0F;
            case DECORATIVE:
                return 4F;
            default:
                return 4F;
        }
    }

    @Override
    public int getToolLevel(Types types) {
        return 2;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        switch (getEnumFromState(world.getBlockState(pos))) {
            case REAL:
                return 6000000.0F;
            case DECORATIVE:
                return 14.0F;
            default:
                return 5;
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        Random rand = world instanceof World ? ((World) world).rand : new Random();
        int count = quantityDropped(state, fortune, rand);
        for (int i = 0; i < count; i++) {
            if (getEnumFromState(world.getBlockState(pos)) == Types.DECORATIVE) {
                ret.add(new ItemStack(HFBlocks.STONE, 1, 1));
            }
        }
        return ret;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }


    //MINE STUFF
    /*
    public static final int CAVE_WALL = 0;
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int side) {
        if (world.rand.nextInt(3) == 0) {
            //MineHelper.caveIn(world, x, y, z);
        }
    }
    
    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        return !EntityHelper.isFakePlayer(player) ? 0.025F : super.getPlayerRelativeBlockHardness(player, world, x, y, z);
    }
    */
}
package joshie.harvest.blocks;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.util.base.CTMBlockHFBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockDirt extends CTMBlockHFBase {
    public BlockDirt(String modid, String texturePath) {
        super(modid, texturePath);
        setCreativeTab(HFTab.MINING);
    }

    //META STUFF
    private static int META_COUNT = 2;

    @Override
    public int getMetaCount() {
        return META_COUNT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        if (General.DEBUG_MODE) {
            for (int i = 0; i < getMetaCount(); i++) {
                if (isValidTab(tab, i)) {
                    list.add(new ItemStack(item, 1, i));
                }
            }
        }
    }

    //TECHNICAL/
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        switch (state.getBlock().getMetaFromState(state)) {
            case 0:
                return 50F;
            case 1:
                return 4F;
            default:
                return 4F;
        }
    }

    @Override
    public int getToolLevel(int meta) {
        return 2;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        IBlockState state = world.getBlockState(pos);
        switch (state.getBlock().getMetaFromState(state)) {
            case 0:
                return 6000;
            case 1:
                return 12.0F;
            default:
                return 5;
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<ItemStack>();

        Random rand = world instanceof World ? ((World) world).rand : new Random();
        int count = quantityDropped(state, fortune, rand);
        for (int i = 0; i < count; i++) {
            if (metadata == 0) {
            }
            if (metadata == 1) {
                ret.add(new ItemStack(HFBlocks.DIRT, 1, 1));
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

    //Normal height = 12 floors, y91 = On a hill = 17 floors, On an extreme hills = y120 = 23 floors
    //private static int MAXIMUM_FLOORS = 23;

    public static enum FloorType {
        ALL_FLOORS, MULTIPLE_OF_5, MULTIPLE_OF_10, MULTIPLE_OF_3, MULTIPLE_OF_2, ENDS_IN_8, ENDS_IN_9, LAST_FLOOR, MYSTRIL_FLOOR, GOLD_FLOOR, MYTHIC_FLOOR, CURSED_FLOOR, NON_MULTIPLE_OF_5, BELOW_15, GODDESS_FLOOR, BERRY_FLOOR;
    }
}
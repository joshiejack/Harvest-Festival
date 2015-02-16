package joshie.harvestmoon.blocks;

import java.util.List;

import joshie.harvestmoon.helpers.MineHelper;
import joshie.harvestmoon.helpers.generic.EntityHelper;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStone extends BlockHMBaseMeta {
    public static final int CAVE_WALL = 0;

    public BlockStone() {
        super(Material.rock);
        setBlockUnbreakable();
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        return !EntityHelper.isFakePlayer(player) ? 0.025F : super.getPlayerRelativeBlockHardness(player, world, x, y, z);
    }
    
    @Override
    public int getToolLevel(int meta) {
        return 1;
    }

    @Override
    public int getMetaCount() {
        return 1;
    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int side) {
        if(world.rand.nextInt(3) == 0) {
            MineHelper.caveIn(world, x, y, z);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        return;
    }
}

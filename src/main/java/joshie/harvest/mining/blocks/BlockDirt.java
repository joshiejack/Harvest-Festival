package joshie.harvest.mining.blocks;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.util.base.BlockHFEnumCube;
import joshie.harvest.core.util.generic.Text;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.blocks.BlockDirt.Types;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.mining.blocks.BlockDirt.Types.DECORATIVE;
import static joshie.harvest.mining.blocks.BlockDirt.Types.REAL;

public class BlockDirt extends BlockHFEnumCube<BlockDirt, Types> {
    public enum Types implements IStringSerializable {
        REAL, DECORATIVE;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockDirt() {
        super(Material.GROUND, Types.class, HFTab.MINING);
        setSoundType(SoundType.GROUND);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack.getItemDamage() == 1) list.add(Text.translate("tooltip.dirt"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        if (General.DEBUG_MODE) {
            super.getSubBlocks(item, tab, list);
        }
    }

    //TECHNICAL/
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return getEnumFromState(state) == REAL ? -1F: 4F;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return getEnumFromState(state) != REAL;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return getEnumFromState(world.getBlockState(pos)) == REAL ? 6000000.0F : 12.0F;
    }

    @Override
    public int getToolLevel(Types types) {
        return 2;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<ItemStack>();
        if (getEnumFromState(world.getBlockState(pos)) == DECORATIVE) {
            ret.add(new ItemStack(HFMining.DIRT, 1, 1));
        }

        return ret;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
        return plantable.getPlantType(world, pos.up()) == EnumPlantType.Plains;
    }
}
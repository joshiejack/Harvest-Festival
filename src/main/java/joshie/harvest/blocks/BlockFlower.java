package joshie.harvest.blocks;

import joshie.harvest.blocks.BlockFlower.FlowerType;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.base.BlockHFBaseEnum;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.AQUA;
import static net.minecraftforge.common.EnumPlantType.Plains;

public class BlockFlower extends BlockHFBaseEnum<FlowerType> implements IPlantable {
    protected static final AxisAlignedBB FLOWER_AABB = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);

    public BlockFlower() {
        super(Material.PLANTS, FlowerType.class, HFTab.TOWN);
        setSoundType(SoundType.GROUND);
    }

    public enum FlowerType implements IStringSerializable {
        GODDESS, WEED;

        public boolean isColored() {
            return this == WEED;
        }

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        IBlockState soil = world.getBlockState(pos.down());
        return super.canPlaceBlockAt(world, pos) && canBlockStay(world, pos, soil);
    }

    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND || state.getBlock() == HFBlocks.FARMLAND;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(world, pos, state, neighborBlock);
        checkAndDropBlock(world, pos, state);
    }

    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
        if (!canBlockStay(world, pos, state)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this) {
            IBlockState soil = world.getBlockState(pos.down());
            return soil.getBlock().canSustainPlant(soil, world, pos.down(), net.minecraft.util.EnumFacing.UP, this);
        }
        return this.canSustainBush(world.getBlockState(pos.down()));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FLOWER_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return Plains;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return getDefaultState();
    }

    @Override
    public int getEntityLifeSpan(ItemStack stack, World world) {
        return stack.getItemDamage() == FlowerType.GODDESS.ordinal() ? 100: 6000;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getEnumFromMeta(stack.getItemDamage()) == FlowerType.GODDESS ? AQUA + super.getItemStackDisplayName(stack) : super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()  {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (getEnumFromMeta(stack.getItemDamage()) == FlowerType.GODDESS) {
            list.add(Translate.translate("tooltip.flower"));
        }
    }

    @SideOnly(Side.CLIENT)
    protected boolean isValidTab(CreativeTabs tab, FlowerType flower) {
        if (flower == FlowerType.WEED) return tab == HFTab.GATHERING;
        return tab == getCreativeTabToDisplayOn();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return -10;
    }
}
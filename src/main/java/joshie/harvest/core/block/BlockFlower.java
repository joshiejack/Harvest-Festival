package joshie.harvest.core.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.entity.EntityBasket;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.interfaces.ISellable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

import static net.minecraft.util.text.TextFormatting.AQUA;
import static net.minecraftforge.common.EnumPlantType.Plains;

public class BlockFlower extends BlockHFEnum<BlockFlower, FlowerType> implements IPlantable {
    private static final AxisAlignedBB FLOWER_AABB = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);

    public BlockFlower() {
        super(Material.PLANTS, FlowerType.class, HFTab.GATHERING);
        setSoundType(SoundType.PLANT);
    }

    public enum FlowerType implements IStringSerializable, ISellable {
        GODDESS(0L), WEED(1L), MOONDROP(60L), TOY(130L),
        PINKCAT(70L), BLUE_MAGICGRASS(80L), RED_MAGICGRASS(200L);

        private final long sell;

        FlowerType(long sell) {
            this.sell = sell;
        }

        @Override
        public long getSellValue() {
            return sell;
        }

        public boolean isColored() {
            return this == WEED;
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(super.getDrops(world, pos, state, fortune));

        EntityPlayer player = harvesters.get();
        if (player != null) {
            EntityBasket.findBasketAndShip(player, list);
        }

        return list;
    }

    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && canBlockStay(world, pos, world.getBlockState(pos));
    }

    private boolean canSustainBush(IBlockState state) {
        return state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        checkAndDropBlock(world, pos, state);
    }

    private void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
        if (!canBlockStay(world, pos, state)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    private boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this) {
            IBlockState soil = world.getBlockState(pos.down());
            return soil.getBlock().canSustainPlant(soil, world, pos.down(), net.minecraft.util.EnumFacing.UP, this);
        }

        return canSustainBush(world.getBlockState(pos.down()));
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FLOWER_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return NULL_AABB;
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
    public int getEntityLifeSpan(@Nonnull ItemStack stack, World world) {
        return stack.getItemDamage() == FlowerType.GODDESS.ordinal() ? 50: 6000;
    }

    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return getEnumFromMeta(stack.getItemDamage()) == FlowerType.GODDESS ? AQUA + super.getItemStackDisplayName(stack) : super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (getEnumFromMeta(stack.getItemDamage()) == FlowerType.GODDESS) {
            list.add(TextHelper.translate("tooltip.flower"));
        }
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        if (getEnumFromStack(stack) == FlowerType.GODDESS) return CreativeSort.TOOLS - 200;
        return CreativeSort.TOOLS - 100;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, property.getName() + "_" + getEnumFromMeta(i).getName()), "inventory"));
        }
    }
}
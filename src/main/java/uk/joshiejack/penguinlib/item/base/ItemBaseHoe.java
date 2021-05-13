package uk.joshiejack.penguinlib.item.base;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EffectHelper;

import javax.annotation.Nonnull;

public abstract class ItemBaseHoe extends ItemHoe implements IPenguinItem {
    protected static final Int2IntMap widthByHarvestLevel = new Int2IntOpenHashMap();
    protected static final Int2IntMap depthByHarvestLevel = new Int2IntOpenHashMap();
    private final String prefix;

    public ItemBaseHoe(ResourceLocation registry) {
        super(ToolMaterial.DIAMOND);
        prefix = registry.getNamespace() + ".item." + registry.getPath();
        widthByHarvestLevel.put(0, 0);
        depthByHarvestLevel.put(0, 0);
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return stack.getItemDamage() == stack.getMaxDamage() ?
                StringHelper.format("penguinlib.item.tools.broken", super.getItemStackDisplayName(stack)) : super.getItemStackDisplayName(stack);
    }

    public ImmutableList<BlockPos> getPositions(EntityPlayer player, World world, BlockPos pos) {
        return ImmutableList.of(pos, pos.down());
    }

    private boolean hoe(EntityPlayer player, World world, BlockPos pos, ItemStack itemstack, EnumFacing facing) {
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, world, pos);
        if (hook != 0) return hook > 0;

        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (facing != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
            if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                this.setBlock(itemstack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                return true;
            }

            if (block == Blocks.DIRT) {
                switch (iblockstate.getValue(BlockDirt.VARIANT)) {
                    case DIRT:
                        setBlock(itemstack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                        return true;
                    case COARSE_DIRT:
                        setBlock(itemstack, player, world, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                        return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void setBlock(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!world.isRemote) {
            world.setBlockState(pos, state, 11);
            stack.damageItem(1, player);
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, @Nonnull World world, BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack) || itemstack.getItemDamage() == itemstack.getMaxDamage()) {
            return EnumActionResult.FAIL;
        } else {
            boolean used = false;
            for (BlockPos target : getPositions(player, world, pos)) {
                if (itemstack.getItemDamage() < itemstack.getMaxDamage()) {
                    if (hoe(player, world, target, itemstack, facing)) {
                        EffectHelper.displayParticle(world, target, EnumParticleTypes.BLOCK_CRACK, Blocks.DIRT.getDefaultState());
                        used = true;
                    }
                } else {
                    player.renderBrokenItemStack(itemstack);
                    break; //Exit the loop as the tool is damaged
                }
            }

            return used ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        }
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return prefix;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
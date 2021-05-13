package uk.joshiejack.penguinlib.item.base;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.item.tools.AreaOfEffect;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;

public abstract class ItemBaseShovel extends ItemSpade implements IPenguinItem {
    protected static final Int2ObjectMap<AreaOfEffect> aoeByHarvestLevel = new Int2ObjectOpenHashMap<>();
    private final String prefix;

    public ItemBaseShovel(ResourceLocation registry, float effiency, float attack) {
        super(ToolMaterial.DIAMOND);
        this.prefix = registry.getNamespace() + ".item." + registry.getPath();
        this.efficiency = effiency;
        this.attackDamage = 1.5F + attack;
        aoeByHarvestLevel.put(0, AreaOfEffect.SINGULAR);
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return stack.getItemDamage() == stack.getMaxDamage() ?
                StringHelper.format("penguinlib.item.tools.broken", super.getItemStackDisplayName(stack)) : super.getItemStackDisplayName(stack);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state) {
        return stack.getItemDamage() < stack.getMaxDamage() ? super.getDestroySpeed(stack, state) : 1F;
    }

    @Nonnull
    public int getChargeLevel(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, World world, @Nonnull IBlockState state, @Nonnull BlockPos position, @Nonnull EntityLivingBase entityLiving) {
        return entityLiving instanceof EntityPlayer && aoeByHarvestLevel.get(getChargeLevel(stack)).onBlockDestroyed(stack, world, position, (EntityPlayer) entityLiving);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, @Nonnull World world, BlockPos position, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        EnumActionResult ret = EnumActionResult.PASS;
        for (BlockPos pos: aoeByHarvestLevel.get(getChargeLevel(itemstack)).getPositions(player, world, position)) {
            if (pos.getY() != position.getY()) continue;
            if (player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
                IBlockState iblockstate = world.getBlockState(pos);
                Block block = iblockstate.getBlock();
                if (facing != EnumFacing.DOWN && world.getBlockState(pos.up()).getMaterial() == Material.AIR && block == Blocks.GRASS) {
                    IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
                    world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    if (!world.isRemote) {
                        world.setBlockState(pos, iblockstate1, 11);
                        itemstack.damageItem(1, player);
                    }

                    ret = EnumActionResult.SUCCESS;
                } else if (ret != EnumActionResult.SUCCESS) {
                    ret = EnumActionResult.PASS;
                }
            }
        }

        return ret;
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
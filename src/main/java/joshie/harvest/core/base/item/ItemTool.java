package joshie.harvest.core.base.item;

import joshie.harvest.api.core.ITiered;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public abstract class ItemTool<I extends ItemTool> extends ItemHFBase<I> implements ITiered, ICreativeSorted {
    private final Set<Block> effectiveBlocks;
    private final ToolTier tier;
    final String toolClass;
    /**
     * Create a tool
     */
    public ItemTool(ToolTier tier, String toolClass, Set<Block> effective) {
        setMaxStackSize(1);
        setHasSubtypes(true);
        setMaxDamage(tier.getMaximumDamage());
        this.effectiveBlocks = effective;
        this.toolClass = toolClass;
        this.tier = tier;
    }

    @Override
    public ToolTier getTier(@Nonnull ItemStack stack) {
        return tier;
    }

    public ItemStack getStack() {
        return new ItemStack(this);
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        return CreativeSort.TOOLS + tier.ordinal();
    }

    @Override
    @Nonnull
    public String getTranslationKey(@Nonnull ItemStack stack) {
        return super.getTranslationKey(stack);
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        String text = TextHelper.localize(super.getTranslationKey().replace("item.", ""));
        return !canUse(stack) ? TextHelper.translate("tool.broken") + " " + text : text;
    }

    @Override
    public double getLevel(@Nonnull ItemStack stack) {
        return stack.getOrCreateSubCompound("Data").getDouble("Level");
    }

    @Override
    public void levelTool(@Nonnull ItemStack stack) {
        double level = stack.getOrCreateSubCompound("Data").getDouble("Level");
        double increase = getLevelIncrease(stack);
        double newLevel = Math.min(100D, level + increase);
        stack.getOrCreateSubCompound("Data").setDouble("Level", newLevel);
    }

    @Override
    public boolean setLevel(@Nonnull ItemStack stack, double newLevel) {
        if (newLevel < 0D || newLevel > 100D) return false;
        else {
            stack.getOrCreateSubCompound("Data").setDouble("Level", newLevel);
            return true;
        }
    }

    @Override
    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return true;
    }

    public boolean canBeDamaged() {
        return true;
    }

    public boolean canUse(@Nonnull ItemStack stack) {
        return getDamage(stack) + 1 <= getMaxDamage(stack) || !canBeDamaged();
    }

    public boolean canLevel(@Nonnull ItemStack stack, IBlockState state) {
        for (String type : getToolClasses(stack)) {
            if (state.getBlock().isToolEffective(type, state))
                return true;
        }

        return effectiveBlocks.contains(state.getBlock());
    }

    @Override
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (canUse(stack) && canBeDamaged()) {
            if (canLevel(stack, state)) {
                ToolHelper.levelTool(stack);
            }

            stack.damageItem(1, entityLiving);
        }

        return true;
    }

    @Override
    @Nonnull
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return 32000;
    }

    public int getFront(ToolTier tier) {
        return 0;
    }

    public int getSides(ToolTier tier) {
        return 0;
    }

    public float getExhaustionRate(@Nonnull ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 3F;
            case COPPER:
                return 2.5F;
            case SILVER:
            case GOLD:
                return 2F;
            case MYSTRIL:
                return 1.5F;
            case CURSED:
                return 25F;
            case BLESSED:
                return 1F;
            case MYTHIC:
                return 0.5F;
            default:
                return 1F;
        }
    }

    private double getLevelIncrease(@Nonnull ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 0.39215682745098D;
            case COPPER:
                return 0.196078431372549D;
            case SILVER:
            case GOLD:
                return 0.130718954248366D;
            case MYSTRIL:
                return 0.0980392156862745D;
            case CURSED:
            case BLESSED:
                return 0.0784313725490196D;
            case MYTHIC:
                return 0.0392156862745098D;
            default:
                return 0;
        }
    }

    protected float getEffiency(@Nonnull ItemStack stack) {
        ToolTier tier = getTier(stack);
        float effiency = 0F;
        switch (tier) {
            case BASIC:
                effiency = 1.5F;
                break;
            case COPPER:
                effiency = 2.5F;
                break;
            case SILVER:
                effiency =  4F;
                break;
            case GOLD:
                effiency =  7F;
                break;
            case MYSTRIL:
                effiency =  10F;
                break;
            case CURSED:
            case BLESSED:
                effiency =  15F;
                break;
            case MYTHIC:
                effiency =  20F;
                break;
        }

        return (float) Math.max(effiency, ((getLevel(stack) + 1) /50F) * effiency);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getHarvestLevel(@Nonnull ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        if (!toolClass.equals(this.toolClass)) return 0;
        if (!canUse(stack)) return 0;
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 1;
            case COPPER:
                return 2;
            case SILVER:
                return 3;
            case GOLD:
                return 4;
            case MYSTRIL:
                return 5;
            case CURSED:
            case BLESSED:
                return 6;
            case MYTHIC:
                return 7;
            default:
                return 0;
        }
    }

    protected int getXMinus(ToolTier tier, EnumFacing facing, int x) {
        if (facing == EnumFacing.NORTH) {
            return x - getSides(tier);
        } else if (facing == EnumFacing.SOUTH) {
            return x - getSides(tier);
        } else if (facing == EnumFacing.EAST) {
            return x - getFront(tier);
        } else return x;
    }

    protected int getXPlus(ToolTier tier, EnumFacing facing, int x) {
        if (facing == EnumFacing.NORTH) {
            return x + getSides(tier);
        } else if (facing == EnumFacing.SOUTH) {
            return x + getSides(tier);
        } else if (facing == EnumFacing.WEST) {
            return x + getFront(tier);
        } else return x;
    }

    protected int getZMinus(ToolTier tier, EnumFacing facing, int z) {
        if (facing == EnumFacing.SOUTH) {
            return z - getFront(tier);
        } else if (facing == EnumFacing.WEST) {
            return z - getSides(tier);
        } else if (facing == EnumFacing.EAST) {
            return z - getSides(tier);
        } else return z;
    }

    protected int getZPlus(ToolTier tier, EnumFacing facing, int z) {
        if (facing == EnumFacing.NORTH) {
            return z + getFront(tier);
        } else if (facing == EnumFacing.WEST) {
            return z + getSides(tier);
        } else if (facing == EnumFacing.EAST) {
            return z + getSides(tier);
        } else return z;
    }

    protected void displayParticle(World world, BlockPos pos, EnumParticleTypes particle, IBlockState state) {
        for (int j = 0; j < 60D; j++) {
            double d8 = (pos.getX()) + world.rand.nextFloat();
            double d9 = (pos.getZ()) + world.rand.nextFloat();
            world.spawnParticle(particle, d8, pos.getY() + 1.0D - 0.125D, d9, 0, 0, 0, Block.getStateId(state));
        }
    }

    protected void playSound(World world, BlockPos pos, SoundEvent sound, SoundCategory category) {
        world.playSound(null, pos, sound, category, world.rand.nextFloat() * 0.25F + 0.75F, world.rand.nextFloat() * 1.0F + 0.5F);
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    protected RayTraceResult getMovingObjectPositionFromPlayer(World world, EntityLivingBase entity) {
        float f = entity.rotationPitch;
        float f1 = entity.rotationYaw;
        double d0 = entity.posX;
        double d1 = entity.posY + (double)entity.getEyeHeight();
        double d2 = entity.posZ;
        Vec3d vec3 = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0D;
        if (entity instanceof EntityPlayerMP) {
            d3 = ((EntityPlayerMP) entity).interactionManager.getBlockReachDistance();
        }

        Vec3d vec31 = vec3.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return world.rayTraceBlocks(vec3, vec31, false, false, false);
    }

    //Tools
    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        switch(getTier(toRepair)) {
            case BASIC: return InventoryHelper.ORE_DICTIONARY.matches(repair, "stone");
            case COPPER: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.COPPER));
            case SILVER: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.SILVER));
            case GOLD: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.GOLD));
            case MYSTRIL: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.MYSTRIL));
            case MYTHIC: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.MYTHIC));
            default: return false;
        }
    }

    @Override
    @Nonnull
    public Set<String> getToolClasses(@Nonnull ItemStack stack) {
        return toolClass != null ? com.google.common.collect.ImmutableSet.of(toolClass) : super.getToolClasses(stack);
    }

    @Override
    public float getStrVsBlock(@Nonnull ItemStack stack, IBlockState state) {
        for (String type : getToolClasses(stack)) {
            if (state.getBlock().isToolEffective(type, state))
                return getEffiency(stack);
        }

        return this.effectiveBlocks.contains(state.getBlock()) ? getEffiency(stack) : 1.0F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@Nonnull ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (HFCore.DEBUG_MODE && advanced) {
            tooltip.add("Level: " + getLevel(stack));
        }
    }
}
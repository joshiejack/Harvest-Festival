package joshie.harvest.core.base;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.ILevelable;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.Text;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public abstract class ItemTool extends ItemHFBase<ItemTool> implements ILevelable, ITiered, ICreativeSorted {
    private final Set<Block> effectiveBlocks;
    private final String toolClass;
    /**
     * Create a tool
     */
    public ItemTool(String toolClass, Set<Block> effective) {
        setMaxStackSize(1);
        setHasSubtypes(true);
        this.effectiveBlocks = effective;
        this.toolClass = toolClass;
    }

    public ItemStack getStack(ToolTier tier) {
        return new ItemStack(this, 1, tier.ordinal());
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TOOLS + getTier(stack).ordinal();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "_" + getTier(stack).name().toLowerCase();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.localize(super.getUnlocalizedName().replace("item.", "") + "." + getTier(stack).name().toLowerCase());
    }

    @Override
    public int getLevel(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 0;
        }

        return (int) stack.getTagCompound().getDouble("Level");
    }

    @Override
    public ToolTier getTier(ItemStack stack) {
        int safe = Math.min(Math.max(0, stack.getItemDamage()), (ToolTier.values().length - 1));
        return ToolTier.values()[safe];
    }

    protected int getMaxCharge(ItemStack stack) {
        return getTier(stack).ordinal();
    }

    protected boolean canCharge(ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound("Data", true);
        int amount = tag.getInteger("Charge");
        return amount < getMaxCharge(stack);
    }

    protected int getCharge(ItemStack stack) {
        return stack.getSubCompound("Data", true).getInteger("Charge");
    }

    protected void setCharge(ItemStack stack, int amount) {
        stack.getSubCompound("Data", true).setInteger("Charge", amount);
    }

    protected void increaseCharge(ItemStack stack, int amount) {
        setCharge(stack, getCharge(stack) + amount);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getDurabilityForDisplay(stack) > 0D;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)  {
        return (double) getDamageForDisplay(stack) / (double)getMaximumToolDamage(stack);
    }

    public boolean canBeDamaged() {
        return true;
    }

    protected int getDamageForDisplay(ItemStack stack) {
        return stack.getSubCompound("Data", true).getInteger("Damage");
    }

    public boolean canUseAndDamage(ItemStack stack) {
        if (getDamageForDisplay(stack) + 1 < getMaximumToolDamage(stack) || !canBeDamaged()) {
            return true;
        } else return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (canBeDamaged()) {
            if (this.effectiveBlocks.contains(state.getBlock())) {
                ToolHelper.levelTool(stack);
            }

            stack.getSubCompound("Data", true).setInteger("Damage", getDamageForDisplay(stack) + 1);
        }

        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn, EnumHand hand) {
        if (canUseAndDamage(stack)) {
            playerIn.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        } else return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (count <= 31995 && count % 32 == 0) {
            if (canCharge(stack)) {
                increaseCharge(stack, 1);
            }
        }
    }

    protected ToolTier getChargeTier(ItemStack stack, int charge) {
        return ToolTier.values()[charge];
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        if (timeLeft <= 31973) {
            int charge = (Math.min(7, Math.max(0, getCharge(stack))));
            setCharge(stack, 0); //Reset the charge
            if (!world.isRemote) {
                onFinishedCharging(world, entity, getMovingObjectPositionFromPlayer(world, entity), stack, getChargeTier(stack, charge));
            }
        }
    }

    protected void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier toolTier) {}

    public int getFront(ToolTier tier) {
        return 0;
    }

    public int getSides(ToolTier tier) {
        return 0;
    }

    public float getExhaustionRate(ItemStack stack) {
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
                return 10F;
            case BLESSED:
                return 1F;
            case MYTHIC:
                return 0.5F;
            default:
                return 1F;
        }
    }

    public double getLevelIncrease(ItemStack stack) {
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

    public int getMaximumToolDamage(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 256;
            case COPPER:
                return 512;
            case SILVER:
                return 1024;
            case GOLD:
                return 2048;
            case MYSTRIL:
                return 4096;
            case CURSED:
            case BLESSED:
                return 8192;
            case MYTHIC:
                return 16384;
            default:
                return 0;
        }
    }

    public float getEffiency(ItemStack stack) {
        ToolTier tier = getTier(stack);
        float effiency = 0F;
        switch (tier) {
            case BASIC:
                effiency = 1.25F;
                break;
            case COPPER:
                effiency = 2.5F;
                break;
            case SILVER:
                effiency =  5F;
                break;
            case GOLD:
                effiency =  7.5F;
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

        return Math.max(effiency, ((getLevel(stack) + 1) /50F) * effiency);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        if (!toolClass.equals(this.toolClass)) return 0;
        if (!canUseAndDamage(stack)) return 0;
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
            world.spawnParticle(particle, d8, pos.getY() + 1.0D - 0.125D, d9, 0, 0, 0, new int[]{Block.getStateId(state)});
        }
    }

    protected void playSound(World world, BlockPos pos, SoundEvent sound, SoundCategory category) {
        world.playSound(null, pos, sound, category, world.rand.nextFloat() * 0.25F + 0.75F, world.rand.nextFloat() * 1.0F + 0.5F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < ToolTier.values().length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
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
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return toolClass != null ? com.google.common.collect.ImmutableSet.of(toolClass) : super.getToolClasses(stack);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        for (String type : getToolClasses(stack)) {
            if (state.getBlock().isToolEffective(type, state))
                return getEffiency(stack);
        }

        return this.effectiveBlocks.contains(state.getBlock()) ? getEffiency(stack) : 1.0F;
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (advanced) {
            tooltip.add("Durability: " + getDamageForDisplay(stack));
            tooltip.add("Level: " + getLevel(stack));
        }
    }
}
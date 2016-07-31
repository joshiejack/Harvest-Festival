package joshie.harvest.core.base;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.ILevelable;
import joshie.harvest.api.core.ITiered;
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

public abstract class ItemBaseTool extends ItemHFBase<ItemBaseTool> implements ILevelable, ITiered, ICreativeSorted {
    /**
     * Create a tool
     */
    public ItemBaseTool() {
        setMaxStackSize(1);
        setHasSubtypes(true);
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
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
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

    protected boolean canCharge(ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound("Data", true);
        int amount = tag.getInteger("Charge");
        return amount < getTier(stack).ordinal();
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
        playerIn.setActiveHand(hand);
        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (count <= 31995 && count % 32 == 0) {
            if (canCharge(stack)) {
                increaseCharge(stack, 1);
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        if (timeLeft <= 31973) {
            int charge = (Math.min(7, Math.max(0, getCharge(stack))));
            setCharge(stack, 0); //Reset the charge
            if (!world.isRemote) {
                onFinishedCharging(world, entity, getMovingObjectPositionFromPlayer(world, entity), stack, ToolTier.values()[charge]);
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

    public double getExhaustionRate(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 3D;
            case COPPER:
                return 2.5D;
            case SILVER:
            case GOLD:
                return 2D;
            case MYSTRIL:
                return 1.5D;
            case CURSED:
                return 10D;
            case BLESSED:
                return 1D;
            case MYTHIC:
                return 0.5D;
            default:
                return 0;
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
}
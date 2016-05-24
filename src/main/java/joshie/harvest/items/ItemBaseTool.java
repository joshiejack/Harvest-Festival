package joshie.harvest.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.ILevelable;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.Translate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

import java.util.List;

public abstract class ItemBaseTool extends ItemBaseSingle implements ILevelable, ITiered, ICreativeSorted {
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
        return Translate.translate(super.getUnlocalizedName().replace("item.", "") + "." + getTier(stack).name().toLowerCase());
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

    public boolean canCharge(ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound("Data", true);
        int amount = tag.getInteger("Charge");
        return amount < getTier(stack).ordinal() + 1;
    }

    public int getCharge(ItemStack stack) {
        return stack.getSubCompound("Data", true).getInteger("Charge");
    }

    public void setCharge(ItemStack stack, int amount) {
        stack.getSubCompound("Data", true).setInteger("Charge", amount);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return canCharge(stack) ? 32 : 0;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn, EnumHand hand) {
        if (canCharge(stack)) {
            System.out.println("Going");
            playerIn.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        } else return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        NBTTagCompound tag = stack.getSubCompound("Data", true);
        tag.setInteger("Charge", tag.getInteger("Charge") + 1);
        return stack;
    }

    public int getFront(ItemStack stack) {
        return 0;
    }

    public int getSides(ItemStack stack) {
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

    protected int getXMinus(ItemStack stack, EnumFacing facing, int x) {
        if (facing == EnumFacing.NORTH) {
            return x - getSides(stack);
        } else if (facing == EnumFacing.SOUTH) {
            return x - getSides(stack);
        } else if (facing == EnumFacing.EAST) {
            return x - getFront(stack);
        } else return x;
    }

    protected int getXPlus(ItemStack stack, EnumFacing facing, int x) {
        if (facing == EnumFacing.NORTH) {
            return x + getSides(stack);
        } else if (facing == EnumFacing.SOUTH) {
            return x + getSides(stack);
        } else if (facing == EnumFacing.WEST) {
            return x + getFront(stack);
        } else return x;
    }

    protected int getZMinus(ItemStack stack, EnumFacing facing, int z) {
        if (facing == EnumFacing.SOUTH) {
            return z - getFront(stack);
        } else if (facing == EnumFacing.WEST) {
            return z - getSides(stack);
        } else if (facing == EnumFacing.EAST) {
            return z - getSides(stack);
        } else return z;
    }

    protected int getZPlus(ItemStack stack, EnumFacing facing, int z) {
        if (facing == EnumFacing.NORTH) {
            return z + getFront(stack);
        } else if (facing == EnumFacing.WEST) {
            return z + getSides(stack);
        } else if (facing == EnumFacing.EAST) {
            return z + getSides(stack);
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
        return world.rayTraceBlocks(vec3, vec31, false, true, false);
    }
}
package joshie.harvest.core.base.item;

import joshie.harvest.api.core.ITiered;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.ICreativeSorted;
import joshie.harvest.core.util.Text;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
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

import java.util.List;
import java.util.Locale;
import java.util.Set;

public abstract class ItemTool<I extends ItemTool> extends ItemHFBase<I> implements ITiered, ICreativeSorted {
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
        return super.getUnlocalizedName(stack) + "_" + getTier(stack).name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.localize(super.getUnlocalizedName().replace("item.", "") + "." + getTier(stack).name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public double getLevel(ItemStack stack) {
        return stack.getSubCompound("Data", true).getDouble("Level");
    }

    @Override
    public void levelTool(ItemStack stack) {
        double level = stack.getSubCompound("Data", true).getDouble("Level");
        double increase = getLevelIncrease(stack);
        double newLevel = Math.min(100D, level + increase);
        stack.getSubCompound("Data", true).setDouble("Level", newLevel);
    }

    @Override
    public ToolTier getTier(ItemStack stack) {
        int safe = Math.min(Math.max(0, stack.getItemDamage()), (ToolTier.values().length - 1));
        return ToolTier.values()[safe];
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

    public boolean canUse(ItemStack stack) {
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

        return (float) Math.max(effiency, ((getLevel(stack) + 1) /50F) * effiency);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
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

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < ToolTier.values().length; i++) {
            list.add(new ItemStack(item, 1, i));
            ItemStack full = new ItemStack(item, 1, i);
            full.getSubCompound("Data", true).setDouble("Level", 100D);
            list.add(full);
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
        switch(getTier(toRepair)) {
            case BASIC: return InventoryHelper.isOreName(repair, "stone");
            case COPPER: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.COPPER));
            case SILVER: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.SILVER));
            case GOLD: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.GOLD));
            case MYSTRIL: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.MYSTRIL));
            case MYTHIC: return InventoryHelper.ITEM_STACK.matches(repair, HFMining.MATERIALS.getStackFromEnum(Material.MYTHIC));
            default: return false;
        }
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
package joshie.harvest.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.ILevelable;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.Translate;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemBaseTool extends ItemBaseSingle implements ILevelable, ITiered, ICreativeSorted {

    /**
     * Create a tool
     */
    public ItemBaseTool() {
        setMaxDamage(8);
        setMaxStackSize(1);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TOOLS + getTier(stack).ordinal();
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

    protected void displayParticle(World world, int x, int y, int z, EnumParticleTypes particle) {
        for (int j = 0; j < 60D; j++) {
            double d8 = (x) + world.rand.nextFloat();
            double d9 = (z) + world.rand.nextFloat();
            world.spawnParticle(particle, d8, y + 1.0D - 0.125D, d9, 0, 0, 0);
        }
    }

    protected void playSound(World world, BlockPos pos, SoundEvent sound, SoundCategory category) {
        world.playSound(null, pos, sound, category, world.rand.nextFloat() * 0.25F + 0.75F, world.rand.nextFloat() * 1.0F + 0.5F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < ToolTier.values().length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
}
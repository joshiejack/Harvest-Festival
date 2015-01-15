package joshie.harvestmoon.items;

import java.util.List;

import joshie.harvestmoon.util.Translate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemBaseTool extends ItemBaseSingle {
    protected static enum ToolTier {
        BASIC, COPPER, SILVER, GOLD, MYSTRIL, CURSED, BLESSED, MYTHIC;
    }

    private IIcon[] icons;

    /** Create a tool */
    public ItemBaseTool() {
        setMaxDamage(8);
        setMaxStackSize(1);
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Translate.translate(super.getUnlocalizedName().replace("item.", "") + "." + getTier(stack).name().toLowerCase());
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }
    
    protected ToolTier getTier(ItemStack stack) {
        int safe = Math.min(Math.max(0, stack.getItemDamage()), (ToolTier.values().length - 1));
        return ToolTier.values()[safe];
    }

    public abstract int getFront(ItemStack stack);

    public abstract int getSides(ItemStack stack);

    protected int getXMinus(ItemStack stack, ForgeDirection facing, int x) {
        if (facing == ForgeDirection.NORTH) {
            return x - getSides(stack);
        } else if (facing == ForgeDirection.SOUTH) {
            return x - getSides(stack);
        } else if (facing == ForgeDirection.EAST) {
            return x - getFront(stack);
        } else return x;
    }

    protected int getXPlus(ItemStack stack, ForgeDirection facing, int x) {
        if (facing == ForgeDirection.NORTH) {
            return x + getSides(stack);
        } else if (facing == ForgeDirection.SOUTH) {
            return x + getSides(stack);
        } else if (facing == ForgeDirection.WEST) {
            return x + getFront(stack);
        } else return x;
    }

    protected int getZMinus(ItemStack stack, ForgeDirection facing, int z) {
        if (facing == ForgeDirection.SOUTH) {
            return z - getFront(stack);
        } else if (facing == ForgeDirection.WEST) {
            return z - getSides(stack);
        } else if (facing == ForgeDirection.EAST) {
            return z - getSides(stack);
        } else return z;
    }

    protected int getZPlus(ItemStack stack, ForgeDirection facing, int z) {
        if (facing == ForgeDirection.NORTH) {
            return z + getFront(stack);
        } else if (facing == ForgeDirection.WEST) {
            return z + getSides(stack);
        } else if (facing == ForgeDirection.EAST) {
            return z + getSides(stack);
        } else return z;
    }

    protected void displayParticle(World world, int x, int y, int z, String particle) {
        for (int j = 0; j < 60D; j++) {
            double d8 = (x) + world.rand.nextFloat();
            double d9 = (z) + world.rand.nextFloat();
            world.spawnParticle(particle, d8, y + 1.0D - 0.125D, d9, 0, 0, 0);
        }
    }

    protected void playSound(World world, int x, int y, int z, String sound) {
        world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), sound, world.rand.nextFloat() * 0.25F + 0.75F, world.rand.nextFloat() * 1.0F + 0.5F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        return icons[damage < icons.length ? damage : 0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[ToolTier.values().length];
        for (int i = 0; i < icons.length; i++) {
            icons[i] = register.registerIcon(path + getUnlocalizedName() + "_" + ToolTier.values()[i].name().toLowerCase());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < ToolTier.values().length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}

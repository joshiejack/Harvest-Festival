package joshie.harvestmoon.items;

import java.util.List;

import joshie.harvestmoon.api.interfaces.ILevelable;
import joshie.harvestmoon.api.interfaces.ITiered;
import joshie.harvestmoon.core.config.Tools;
import joshie.harvestmoon.core.util.Translate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemBaseTool extends ItemBaseSingle implements ILevelable, ITiered {
    public static enum ToolTier {
        BASIC("WOOD"), COPPER("GOLD"), SILVER("STONE"), GOLD("IRON"), MYSTRIL("DIAMOND"), CURSED, BLESSED, MYTHIC;

        private String alt;

        private ToolTier() {}

        private ToolTier(String alt) {
            this.alt = alt;
        }

        public String getName() {
            return Tools.USE_VANILLA_MATERIALS && alt != null ? alt : name();
        }
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
                return 0.5D;
            case COPPER:
                return 1D;
            case SILVER:
            case GOLD:
                return 1.5D;
            case MYSTRIL:
                return 2D;
            case CURSED:
                return 10D;
            case BLESSED:
                return 2.5D;
            case MYTHIC:
                return 3D;
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

    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack) {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) {
            float f = 0.7F;
            double d0 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double) x + d0, (double) y + d1, (double) z + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
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
            icons[i] = register.registerIcon(path + getUnlocalizedName().replace("item.", "") + "_" + ToolTier.values()[i].getName().toLowerCase());
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

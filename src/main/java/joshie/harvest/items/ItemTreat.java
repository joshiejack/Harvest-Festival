package joshie.harvest.items;

import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.core.helpers.AnimalHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTreat extends ItemHFMeta implements ICreativeSorted {
    public static final int COW = 0;
    public static final int SHEEP = 1;
    public static final int CHICKEN = 2;

    public static IAnimalType getTreatTypeFromMeta(int meta) {
        switch (meta) {
            case COW:
                return AnimalRegistry.cow;
            case SHEEP:
                return AnimalRegistry.sheep;
            case CHICKEN:
                return AnimalRegistry.chicken;
            default:
                return null;
        }
    }

    @Override
    public int getMetaCount() {
        return 3;
    }

    @Override
    public String getName(ItemStack stack) {
        return getTreatTypeFromMeta(stack.getItemDamage()).getName();
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase living) {
        if (living instanceof EntityAnimal) {
            if (!living.worldObj.isRemote) {
                AnimalHelper.treat(stack, player, (EntityAnimal) living);
            }

            stack.stackSize--;

            return true;
        } else return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        String path = this.path != null ? this.path : mod + ":";
        icons = new IIcon[getMetaCount()];
        for (int i = 0; i < icons.length; i++) {
            icons[i] = register.registerIcon(HFModInfo.TREATPATH + getName(new ItemStack(this, 1, i)));
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 100;
    }
}

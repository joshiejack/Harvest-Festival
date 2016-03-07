package joshie.harvest.items;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTreat extends ItemHFMeta implements ICreativeSorted {
    public static final int COW = 0;
    public static final int SHEEP = 1;
    public static final int CHICKEN = 2;
    public static final int GENERIC = 3;

    public static IAnimalType getTreatTypeFromStack(ItemStack stack) {
        return HFApi.ANIMALS.getTypeFromString(HFItems.treats.getName(stack));
    }

    @Override
    public int getMetaCount() {
        return 4;
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case COW:
                return "cow";
            case SHEEP:
                return "sheep";
            case CHICKEN:
                return "chicken";
            case GENERIC:
                return "generic";
            default:
                return "null";
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase living) {
        if (living instanceof IAnimalTracked) {
            if (!living.worldObj.isRemote) {
                if (((IAnimalTracked) living).getData().treat(stack, player)) {
                    stack.stackSize--;
                }
            }

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

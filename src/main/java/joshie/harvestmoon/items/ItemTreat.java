package joshie.harvestmoon.items;

import joshie.harvestmoon.animals.AnimalData;
import joshie.harvestmoon.helpers.AnimalHelper;
import joshie.harvestmoon.lib.HMModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTreat extends ItemHMMeta {
    @Override
    public int getMetaCount() {
        return AnimalData.AnimalType.values().length;
    }

    @Override
    public String getName(ItemStack stack) {
        if (stack.getItemDamage() < getMetaCount()) {
            return AnimalData.AnimalType.values()[stack.getItemDamage()].name().toLowerCase();
        } else return AnimalData.AnimalType.OTHER.name().toLowerCase();
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase living) {
        if (living instanceof EntityAnimal) {
            if (!living.worldObj.isRemote) {
                AnimalHelper.treat(stack, player, living);
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
            icons[i] = register.registerIcon(HMModInfo.TREATPATH + getName(new ItemStack(this, 1, i)));
        }
    }
}

package joshie.harvestmoon.items;

import static joshie.harvestmoon.HarvestMoon.handler;
import joshie.harvestmoon.animals.AnimalData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

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
                handler.getServer().getAnimalTracker().treat(stack, player, (EntityAnimal) living);
            }

            stack.stackSize--;

            return true;
        } else return false;
    }
}

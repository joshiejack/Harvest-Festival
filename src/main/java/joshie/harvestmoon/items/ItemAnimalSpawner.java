package joshie.harvestmoon.items;

import joshie.harvestmoon.animals.EntityHarvestCow;
import joshie.harvestmoon.core.HMTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAnimalSpawner extends ItemHMMeta {
    public ItemAnimalSpawner() {
        super(HMTab.tabFarming);
    }

    @Override
    public int getMetaCount() {
        return 1;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "cow";
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && stack.getItemDamage() < getMetaCount()) {
            EntityHarvestCow entity = new EntityHarvestCow(world);
            entity.setPosition(xCoord + 0.5, yCoord + 1, zCoord + 0.5);
            world.spawnEntityInWorld(entity);
        }

        return false;
    }
}

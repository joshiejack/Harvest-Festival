package joshie.harvestmoon.items;

import joshie.harvestmoon.animals.EntityHarvestCow;
import joshie.harvestmoon.animals.EntityHarvestSheep;
import joshie.harvestmoon.core.HMTab;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAnimalSpawner extends ItemHMMeta {
    public static final int COW = 0;
    public static final int SHEEP = 1;

    public ItemAnimalSpawner() {
        super(HMTab.tabFarming);
    }

    @Override
    public int getMetaCount() {
        return 2;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "cow";
    }

    public EntityAgeable getEntityFromMeta(World world, int meta) {
        switch (meta) {
            case COW:
                return new EntityHarvestCow(world);
            case SHEEP:
                return new EntityHarvestSheep(world);
            default:
                return null;
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && stack.getItemDamage() < getMetaCount()) {
            EntityAgeable entity = getEntityFromMeta(world, stack.getItemDamage());
            entity.setPosition(xCoord + 0.5, yCoord + 1, zCoord + 0.5);
            world.spawnEntityInWorld(entity);
        }

        return false;
    }
}

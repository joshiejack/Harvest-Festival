package joshie.harvestmoon.items;

import joshie.harvestmoon.buildings.Building;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBuilding extends ItemHMMeta {
    @Override
    public int getMetaCount() {
        return Building.buildings.size();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return Building.buildings.get(stack.getItemDamage()).generate(world, x, y, z);
    }
}

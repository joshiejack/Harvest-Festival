package joshie.harvestmoon.items;

import joshie.harvestmoon.buildings.BuildingGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBuilding extends ItemHMMeta {
    @Override
    public int getMetaCount() {
        return BuildingGroup.groups.size();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        BuildingGroup group = BuildingGroup.groups.get(stack.getItemDamage());
        if (group != null) {
            return group.getRandom().generate(world, x, y, z);
        } else return false;
    }

    @Override
    public String getName(ItemStack stack) {
        if (stack.getItemDamage() >= BuildingGroup.groups.size()) return "invalid";
        BuildingGroup group = BuildingGroup.groups.get(stack.getItemDamage());
        if (group != null) {
            return group.getName();
        } else return "invalid";
    }
}

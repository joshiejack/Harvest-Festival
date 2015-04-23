package joshie.harvestmoon.items;

import static joshie.harvestmoon.core.lib.HMModInfo.BUILDINGPATH;
import joshie.harvestmoon.api.core.ICreativeSorted;
import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.helpers.UUIDHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBuilding extends ItemHMMeta implements ICreativeSorted {
    public ItemBuilding() {
        super(HMTab.tabTown);
        setTextureFolder(BUILDINGPATH);
    }
    
    @Override
    public int getMetaCount() {
        return BuildingGroup.groups.size();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        BuildingGroup group = BuildingGroup.groups.get(stack.getItemDamage());
        if (group != null) {
            return group.getRandom().generate(UUIDHelper.getPlayerUUID(player), world, x, y, z);
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
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "[SPAWN] " + super.getItemStackDisplayName(stack); 
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 200;
    }
}

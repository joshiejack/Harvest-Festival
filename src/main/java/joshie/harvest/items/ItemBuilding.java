package joshie.harvest.items;

import static joshie.harvest.core.lib.HFModInfo.BUILDINGPATH;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.buildings.Building;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.UUIDHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBuilding extends ItemHFMeta implements ICreativeSorted {
    public ItemBuilding() {
        super(HFTab.tabTown);
        setTextureFolder(BUILDINGPATH);
    }
    
    @Override
    public int getMetaCount() {
        return Building.buildings.size();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Building group = Building.buildings.get(stack.getItemDamage());
        if (group != null) {
            return group.generate(UUIDHelper.getPlayerUUID(player), world, x, y, z);
        } else return false;
    }

    @Override
    public String getName(ItemStack stack) {
        if (stack.getItemDamage() >= Building.buildings.size()) return "invalid";
        Building group = Building.buildings.get(stack.getItemDamage());
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

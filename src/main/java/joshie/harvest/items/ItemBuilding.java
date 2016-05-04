package joshie.harvest.items;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.buildings.Building;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.util.base.ItemHFBaseMeta;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBuilding extends ItemHFBaseMeta implements ICreativeSorted {
    public ItemBuilding() {
        super(HFTab.TOWN);
    }

    @Override
    public int getMetaCount() {
        return Building.buildings.size();
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBuilding group = Building.buildings.get(stack.getItemDamage());
        if (group != null) {
            return group.generate(UUIDHelper.getPlayerUUID(player), world, pos);
        }

        return EnumActionResult.PASS;
    }

    @Override
    public String getName(ItemStack stack) {
        if (stack.getItemDamage() >= Building.buildings.size()) return "invalid";
        IBuilding group = Building.buildings.get(stack.getItemDamage());
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

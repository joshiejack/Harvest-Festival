package joshie.harvest.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.buildings.Building;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.UUIDHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.core.lib.HFModInfo.BUILDINGPATH;

public class ItemBuilding extends ItemHFMeta implements ICreativeSorted {
    public ItemBuilding() {
        super(HFTab.TOWN);
        setTextureFolder(BUILDINGPATH);
    }
    
    @Override
    public int getMetaCount() {
        return Building.buildings.size();
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Building group = Building.buildings.get(stack.getItemDamage());
        IBlockState state = world.getBlockState(pos);
        if (group != null) {
            //return group.generate(UUIDHelper.getPlayerUUID(player), world, pos, state); //TODO
        } /*else*/ return EnumActionResult.PASS;
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

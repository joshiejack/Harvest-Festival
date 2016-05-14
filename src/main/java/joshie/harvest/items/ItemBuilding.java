package joshie.harvest.items;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.buildings.loader.HFBuildings;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.ItemHFBaseMeta;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBuilding extends ItemHFBaseMeta implements ICreativeSorted {
    public ItemBuilding() {
        super(HFTab.TOWN);
    }

    public ItemStack getItemStack(IBuilding building) {
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("Building", building.getResource().toString());
        return stack;
    }

    @Override
    public int getMetaCount() {
        return HFApi.buildings.getBuildings().size();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getName(stack);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBuilding group = getBuildingFromStack(stack);
        if (group != null) {
            return group.getProvider().generate(world, pos);
        }

        return EnumActionResult.PASS;
    }

    public IBuilding getBuildingFromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) return HFBuildings.null_building;
        IBuilding building = HFApi.buildings.getBuildingFromName(new ResourceLocation(stack.getTagCompound().getString("Building")));
        return building == null ? HFBuildings.null_building: building;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "[SPAWN] " + getBuildingFromStack(stack).getLocalisedName();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 200;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        if (tab == HFTab.TOWN) {
            for (IBuilding building : HFApi.buildings.getBuildings()) {
                if (building == null) continue;
                list.add(getItemStack(building));
            }
        }
    }
}

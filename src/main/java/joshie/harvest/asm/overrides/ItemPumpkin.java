package joshie.harvest.asm.overrides;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemPumpkin extends ItemBlock implements IShippable, ICropProvider, ICreativeSorted {
    public ItemPumpkin(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.CROPS;
    }

    @Override
    public ICrop getCrop(ItemStack stack) {
        return ItemSeedFood.getCrop(stack);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return ItemSeedFood.getSellValue(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return ItemSeedFood.getItemStackDisplayName(stack);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, block.getDefaultState(), 2)) {
            return false;
        }

        if (world.getBlockState(pos).getBlock() == block) {
            block.onBlockPlacedBy(world, pos, newState, player, stack);
            block.onBlockPlaced(world, pos, side, hitX, hitY, hitZ, block.getMetaFromState(newState), player);
            HFTrackers.getCropTracker().plantCrop(player, world, pos, HFCrops.pumpkin, 1);
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        ItemSeedFood.getSubItems(item, tab, list);
    }
}
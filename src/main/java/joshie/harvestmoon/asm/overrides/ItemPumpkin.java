package joshie.harvestmoon.asm.overrides;

import java.util.List;

import joshie.harvestmoon.api.core.ICreativeSorted;
import joshie.harvestmoon.api.core.IRateable;
import joshie.harvestmoon.api.core.IShippable;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropProvider;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.lib.CreativeSort;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPumpkin extends ItemBlock implements IRateable, IShippable, ICropProvider, ICreativeSorted {
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
    public int getRating(ItemStack stack) {
        return ItemSeedFood.getRating(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return ItemSeedFood.getItemStackDisplayName(stack);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, field_150939_a, metadata, 3)) {
            return false;
        }

        if (world.getBlock(x, y, z) == field_150939_a) {
            field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
            System.out.println("I PLACE");
            CropHelper.plantCrop(player, world, x, y, z, HMCrops.pumpkin, 1 - CropHelper.getCropQualityFromDamage(stack.getItemDamage()));
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemSeedFood.getSubItems(item, tab, list);
    }
}

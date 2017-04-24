package joshie.harvest.cooking.item;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFBase;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemCookbook extends ItemHFBase<ItemCookbook> implements ICreativeSorted {
    public ItemCookbook() {
        super(HFTab.COOKING);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (!player.isSneaking()) {
            player.openGui(HarvestFestival.instance, GuiHandler.COOKBOOK, world, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        } else return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 1000;
    }
}

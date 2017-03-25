package joshie.harvest.cooking.item;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
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

public class ItemCookbook extends ItemHFBase<ItemCookbook> implements ICreativeSorted {
    public ItemCookbook() {
        super(HFTab.COOKING);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!player.isSneaking()) {
            player.openGui(HarvestFestival.instance, GuiHandler.COOKBOOK, world, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 1000;
    }





}

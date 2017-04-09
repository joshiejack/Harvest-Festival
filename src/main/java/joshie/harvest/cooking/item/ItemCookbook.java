package joshie.harvest.cooking.item;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFBase;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemCookbook extends ItemHFBase<ItemCookbook> implements ICreativeSorted {
    public ItemCookbook() {
        super(HFTab.COOKING);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!player.isSneaking()) {
            player.openGui(HarvestFestival.instance, GuiHandler.COOKBOOK, world, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            boolean availableOnly = stack.hasTagCompound() && stack.getTagCompound().getBoolean("Available");
            NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            tag.setBoolean("Available", !availableOnly);
            stack.setTagCompound(tag);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 1000;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("ConstantConditions")
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        boolean availableOnly = stack.hasTagCompound() && stack.getTagCompound().getBoolean("Available");
        if (availableOnly) {
            tooltip.add(TextFormatting.GREEN + TextHelper.translate("cook.mode.cookable"));
        } else tooltip.add(TextFormatting.AQUA + TextHelper.translate("cook.mode.any"));
        tooltip.add(TextFormatting.ITALIC + TextHelper.translate("cook.mode.switch"));
    }
}

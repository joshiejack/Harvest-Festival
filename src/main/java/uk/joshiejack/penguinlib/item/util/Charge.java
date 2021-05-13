package uk.joshiejack.penguinlib.item.util;

import uk.joshiejack.penguinlib.data.custom.material.CustomToolMaterialData;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ChatHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
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
import java.util.Locale;

public class Charge {
    private final int harvestLevel;
    public final String prefix;
    public final EnumAction action = EnumAction.BOW;
    public final int duration = 32;

    public Charge(int tier, String tool) {
        this.harvestLevel = tier;
        this.prefix = "harvestfestival.item." + tool + ".tooltip.charge.";
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public int getChargeLevel(ItemStack stack) {
        return NBTHelper.getItemNBT(stack).getByte("ChargeLevel");
    }

    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack) {
        NBTTagCompound tag = NBTHelper.getItemNBT(stack);
        int level = tag.getByte("ChargeLevel");
        if (level < harvestLevel) {
            CustomToolMaterialData material = CustomToolMaterialData.byInt.get(level);
            tag.setByte("ChargeLevel", (byte) material.next().harvestLevel);
            ChatHelper.displayChat(TextFormatting.GREEN + StringHelper.format("harvestfestival.item.tools.message.charge", material.next().name));
        }

        return stack;
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack tool = player.getHeldItem(hand);
        if (player.isSneaking()) {
            NBTTagCompound tag = NBTHelper.getItemNBT(tool);
            tag.setByte("ChargeLevel", (byte) 0);
            if (world.isRemote) { //Tell the client that we discharged the tool
                ChatHelper.displayChat(TextFormatting.RED + StringHelper.localize("harvestfestival.item.tools.message.discharge"));
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, tool);
        } else {
            NBTTagCompound tag = NBTHelper.getItemNBT(tool);
            int level = tag.getByte("ChargeLevel");
            if (level < harvestLevel) {
                player.setActiveHand(hand);
                return new ActionResult<>(EnumActionResult.SUCCESS, tool);
            } else return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        NBTTagCompound tag = NBTHelper.getItemNBT(stack);
        int internal = tag.getByte("ChargeLevel");
        if (harvestLevel != 0) {
            tooltip.add(TextFormatting.GOLD + StringHelper.localize(prefix + CustomToolMaterialData.byInt.get(internal).name.toLowerCase(Locale.ENGLISH)));
            if (internal < this.harvestLevel)
                tooltip.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + StringHelper.localize("harvestfestival.item.tools.tooltip.charge"));
            if (internal != 0)
                tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + StringHelper.localize("harvestfestival.item.tools.tooltip.discharge"));
        }
    }
}

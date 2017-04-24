package joshie.harvest.core.base.item;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public class ItemToolChargeable<I extends ItemToolChargeable> extends ItemTool<I> {
    public static final TIntObjectMap<ToolTier> LEVEL_TO_TIER = new TIntObjectHashMap<>();
    static {
        for (ToolTier tier: ToolTier.values()) {
            if (tier == ToolTier.BLESSED) continue;
            LEVEL_TO_TIER.put(tier.getToolLevel(), tier);
        }
    }

    public ItemToolChargeable(String toolClass, Set<Block> effective) {
        super(toolClass, effective);
    }

    protected int getMaxCharge(ItemStack stack) {
        return getTier(stack).getToolLevel();
    }

    protected boolean canCharge(ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound("Data", true);
        int amount = tag.getInteger("Charge");
        return amount < getMaxCharge(stack);
    }

    protected int getCharge(ItemStack stack) {
        return stack.getSubCompound("Data", true).getInteger("Charge");
    }

    protected void setCharge(ItemStack stack, int amount) {
        stack.getSubCompound("Data", true).setInteger("Charge", amount);
    }

    protected void increaseCharge(ItemStack stack, int amount) {
        setCharge(stack, getCharge(stack) + amount);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn, EnumHand hand) {
        ToolTier tier = getTier(stack);
        if (tier != ToolTier.BASIC && canUse(stack)) {
            if (playerIn.isSneaking()) {
                setCharge(stack, 0);
                if (world.isRemote) ChatHelper.displayChat(TextFormatting.RED + TextHelper.translate("tool.discharge"));
            } else if (getCharge(stack) < getMaxCharge(stack)) playerIn.setActiveHand(hand);
            else onPlayerStoppedUsing(stack, world, playerIn, 32000);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else if (tier == ToolTier.BASIC && canUse(stack)) {
            onPlayerStoppedUsing(stack, world, playerIn, 32000);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    private int getCharges(int count) {
        int passed = 32000 - count;
        return passed / 20;
    }

    protected String getLevelName(ItemStack stack, int charges) {
        int maximum = getMaxCharge(stack);
        int charge = getCharge(stack);
        int newCharge = Math.min(maximum, charge + charges);
        return charge == newCharge ? null : "" + newCharge;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (count != 32000 && count % 20 == 0) {
            if (player.world.isRemote) {
                String name =  getLevelName(stack, getCharges(count));
                if (name != null) {
                    ChatHelper.displayChat(TextFormatting.GREEN + TextHelper.formatHF("tool.charge", name));
                }
            }
        }
    }

    protected ToolTier getChargeTier(int charge) {
        return LEVEL_TO_TIER.get(charge);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        int maximum = getMaxCharge(stack);
        int charge = getCharge(stack);
        int newCharge = Math.min(maximum, charge + getCharges(timeLeft));
        if (charge < maximum) {
            setCharge(stack, newCharge);
        }

        onFinishedCharging(world, entity, getMovingObjectPositionFromPlayer(world, entity), stack, getChargeTier(newCharge));
    }

    protected void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier toolTier) {}
}

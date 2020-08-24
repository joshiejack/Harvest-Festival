package joshie.harvest.core.base.item;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class ItemToolChargeable<I extends ItemToolChargeable> extends ItemTool<I> {
    protected static final TIntObjectMap<ToolTier> LEVEL_TO_TIER = new TIntObjectHashMap<>();
    static {
        for (ToolTier tier: ToolTier.values()) {
            if (tier == ToolTier.BLESSED) continue;
            LEVEL_TO_TIER.put(tier.getToolLevel(), tier);
        }
    }

    public ItemToolChargeable(ToolTier tier, String toolClass, Set<Block> effective) {
        super(tier, toolClass, effective);
    }

    protected int getMaxCharge(@Nonnull ItemStack stack) {
        return getTier(stack).getToolLevel();
    }

    protected int getCharge(@Nonnull ItemStack stack) {
        return stack.getOrCreateSubCompound("Data").getInteger("Charge");
    }

    private void setCharge(@Nonnull ItemStack stack, int amount) {
        stack.getOrCreateSubCompound("Data").setInteger("Charge", amount);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerIn, @Nonnull EnumHand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);
        ToolTier tier = getTier(stack);
        if (tier != ToolTier.BASIC && canUse(stack)) {
            if (playerIn.isSneaking()) {
                setCharge(stack, 0);
                if (world.isRemote && MCClientHelper.isClient(playerIn)) ChatHelper.displayChat(TextFormatting.RED + TextHelper.translate("tool.discharge"));
            } else if (getCharge(stack) < getMaxCharge(stack)) playerIn.setActiveHand(hand);
            else onPlayerStoppedUsing(stack, world, playerIn, 32000);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else if (tier == ToolTier.BASIC && canUse(stack)) {
            onPlayerStoppedUsing(stack, world, playerIn, 32000);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    private int getCharges(int count) {
        int passed = 32000 - count;
        return passed / 20;
    }

    protected String getLevelName(@Nonnull ItemStack stack, int charges) {
        int maximum = getMaxCharge(stack);
        int charge = getCharge(stack);
        int newCharge = Math.min(maximum, charge + charges);
        return charge == newCharge ? null : "" + newCharge;
    }

    @Override
    public void onUsingTick(@Nonnull ItemStack stack, EntityLivingBase player, int count) {
        if (count != 32000 && count % 20 == 0) {
            if (player.world.isRemote && MCClientHelper.isClient(player)) {
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
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        int maximum = getMaxCharge(stack);
        int charge = getCharge(stack);
        int newCharge = Math.min(maximum, charge + getCharges(timeLeft));
        if (charge < maximum) {
            setCharge(stack, newCharge);
        }

        onFinishedCharging(world, entity, getMovingObjectPositionFromPlayer(world, entity), stack, getChargeTier(newCharge));
    }

    protected void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, @Nonnull ItemStack stack, ToolTier toolTier) {}
}
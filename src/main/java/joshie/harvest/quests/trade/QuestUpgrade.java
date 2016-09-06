package joshie.harvest.quests.trade;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.helpers.generic.ItemHelper.spawnXP;
import static joshie.harvest.npc.HFNPCs.TOOL_OWNER;


@HFQuest("trade.upgrade")
public class QuestUpgrade extends QuestTrade {
    private CalendarDate date;
    private ItemStack tool;
    private long required;
    private ItemStack material;

    public QuestUpgrade() {
        setNPCs(TOOL_OWNER);
    }

    private int getDifference(CalendarDate then, CalendarDate now) {
        int thenDays = CalendarHelper.getTotalDays(then);
        int nowDays = CalendarHelper.getTotalDays(now);
        return (nowDays - thenDays);
    }

    private long getCost(ToolTier tier) {
        switch (tier) {
            case BASIC:
                return 1000;
            case COPPER:
                return 2000;
            case SILVER:
                return 5000;
            case GOLD:
                return 10000;
            case BLESSED:
                return 100000;
            default:
                return 0;
        }
    }

    private int getRequired(ToolTier tier) {
        switch (tier) {
            case BASIC:
            case COPPER:
            case SILVER:
                return 10;
            case GOLD:
                return 5;
            case BLESSED:
                return 1;
            default:
                return 0;
        }
    }

    private int getMaterial(ToolTier tier) {
        switch (tier) {
            case BASIC:
                return Material.COPPER.ordinal();
            case COPPER:
                return Material.SILVER.ordinal();
            case SILVER:
                return Material.GOLD.ordinal();
            case GOLD:
                return Material.MYSTRIL.ordinal();
            case BLESSED:
                return Material.MYTHIC.ordinal();
            default:
                return 0;
        }
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalized(String quest) {
        if (quest.equals("gold")) {
            //We're adding the amount that you need
            return I18n.translateToLocalFormatted("harvestfestival.quest.trade.upgrade.gold", required);
        } else if (quest.equals("material")) {
            return I18n.translateToLocalFormatted("harvestfestival.quest.trade.upgrade.material", material.stackSize, material.getDisplayName());
        } else if (quest.equals("wait")) {
            CalendarDate today = HFApi.calendar.getDate(MCClientHelper.getWorld());
            return I18n.translateToLocalFormatted("harvestfestival.quest.trade.upgrade.wait", 3 - (getDifference(date, today)));
        } else if (quest.equals("done")) {
            return I18n.translateToLocalFormatted("harvestfestival.quest.trade.upgrade.done", tool.getDisplayName());
        }else return super.getLocalized(quest);
    }

    private double getLevel(EntityPlayer player) {
        ItemStack held = player.getHeldItemMainhand();
        if (held == null) return 0D;
        if (held.getItem() instanceof ITiered) {
            return ((ITiered)held.getItem()).getLevel(held);
        } else return 0D;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == 0) {
            ToolTier holding = isHolding(player);
            if (holding != null) {
                //Blacksmith complains that the tool isn't even ready to be upgraded, it needs to be at 100%
                if (getLevel(player) != 100D) return "level";

                required = getCost(holding);
                boolean hasGold = HFTrackers.getPlayerTrackerFromPlayer(player).getStats().getGold() >= required;
                //Blacksmith complains about not having enough gold to complete the upgrade
                if (!hasGold) return "gold";

                material = new ItemStack(HFMining.MATERIALS, getRequired(holding), getMaterial(holding));
                boolean hasMaterial = InventoryHelper.getCount(player, material) >= getRequired(holding);
                if (!hasMaterial) return "material";

                //The blacksmith thanks the player for the gold, and their tool
                //He informs them he will have it upgraded within three days
                //So come back for it then
                increaseStage(player);
                return "accept";
            } else return null;
        } else {
            CalendarDate today = HFApi.calendar.getDate(MCClientHelper.getWorld());
            if (getDifference(date, today) >= 3) {
                complete(player);
                player.worldObj.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.NEUTRAL, 0.25F, 1F);
                for (int i = 0; i < 32; i++) {
                    player.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entity.posX + player.worldObj.rand.nextFloat() + player.worldObj.rand.nextFloat() - 1F, entity.posY + 0.25D + entity.worldObj.rand.nextFloat() + entity.worldObj.rand.nextFloat(), entity.posZ + player.worldObj.rand.nextFloat() + player.worldObj.rand.nextFloat() - 1F, 0, 0, 0);
                }

                return "done";
            }

            return "wait";
        }
    }

    @Override
    public void onStageChanged(EntityPlayer player, int previous, int stage) {
        if (previous == 0) {
            ToolTier holding = isHolding(player);
            InventoryHelper.takeItems(player, new ItemStack(HFMining.MATERIALS, getRequired(holding), getMaterial(holding)));
            date = HFApi.calendar.getDate(player.worldObj).copy();
            ItemStack stack = player.getHeldItemMainhand().copy();
            tool = new ItemStack(stack.getItem(), 1, stack.getItemDamage() + 1);
            ToolTier tier = ((ItemTool)stack.getItem()).getTier(stack);
            rewardGold(player, -getCost(tier));
            takeHeldStack(player, 1);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, tool);
        spawnXP(player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ, 5);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Date")) {
            date = CalendarDate.fromNBT(nbt.getCompoundTag("Date"));
            tool = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item"));
        }
    }

    /** Called to write data about this quest
     * @param nbt the nbt tag to write to
     * @return the nbt tag**/
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (date != null) {
            nbt.setTag("Date", date.toNBT());
            nbt.setTag("Item", tool.writeToNBT(new NBTTagCompound()));
        }

        return nbt;
    }

    private ToolTier isHolding(EntityPlayer player) {
        ItemStack held = player.getHeldItemMainhand();
        if (held != null) {
            if (held.getItem() instanceof ItemTool) {
                ItemTool tool = ((ItemTool)held.getItem());
                ToolTier tier = tool.getTier(held);
                return getCost(tier) > 0 ? tier : null;
            }
        }

        return null;
    }
}

package joshie.harvest.quests.trade;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;
import static joshie.harvest.core.helpers.SpawnItemHelper.spawnXP;
import static joshie.harvest.npc.HFNPCs.TOOL_OWNER;


@HFQuest("trade.upgrade")
public class QuestUpgrade extends QuestTrade {
    private static final int TEST = 0;
    private CalendarDate date;
    private ItemStack tool;
    private int days;

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
            case MYTHIC:
                return 200000;
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

    private ItemStack getUpgradeMaterial(ToolTier tier) {
        switch (tier) {
            case BASIC:
                return new ItemStack(Blocks.STONE);
            case COPPER:
                return HFMining.MATERIALS.getStackFromEnum(Material.COPPER);
            case SILVER:
                return HFMining.MATERIALS.getStackFromEnum(Material.SILVER);
            case GOLD:
                return HFMining.MATERIALS.getStackFromEnum(Material.GOLD);
            case MYSTRIL:
                return HFMining.MATERIALS.getStackFromEnum(Material.MYSTRIL);
            case MYTHIC:
                return HFMining.MATERIALS.getStackFromEnum(Material.MYTHIC);
            default:
                return null;
        }
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
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == TEST) {
            //Repairing
            ToolTier broken = isHoldingBrokenTool(player);
            if (broken != null) {
                long required = getCost(broken) / 10;
                if (HFTrackers.getPlayerTrackerFromPlayer(player).getStats().getGold() < required) return getLocalized("repair.gold", required);
                ItemStack material = getUpgradeMaterial(broken);
                if (InventoryHelper.hasInInventory(player, ITEM_STACK, material)) {
                    return getLocalized("repair.start");
                }

                return getLocalized("repair.material", material.getDisplayName());
            }

            //Upgrading
            ToolTier holding = isHolding(player);
            if (holding != null) {
                //Blacksmith complains that the tool isn't even ready to be upgraded, it needs to be at 100%
                if (getLevel(player) != 100D) return getLocalized("level");

                long required = getCost(holding);
                boolean hasGold = HFTrackers.getPlayerTrackerFromPlayer(player).getStats().getGold() >= required;
                //Blacksmith complains about not having enough gold to complete the upgrade
                if (!hasGold) return getLocalized("gold", required);

                ItemStack material = new ItemStack(HFMining.MATERIALS, getRequired(holding), getMaterial(holding));
                boolean hasMaterial = InventoryHelper.hasInInventory(player, ITEM_STACK, material, getRequired(holding));
                if (!hasMaterial) return getLocalized("material", material.stackSize, material.getDisplayName());

                //The blacksmith thanks the player for the gold, and their tool
                //He informs them he will have it upgraded within three days
                //So come back for it then
                return getLocalized("accept");
            } else return null;
        } else {
            CalendarDate today = HFApi.calendar.getDate(player.worldObj);
            if (getDifference(date, today) >= 3) {
                return getLocalized("done", tool.getDisplayName());
            }

            return getLocalized("wait", days - (getDifference(date, today)));
        }
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == TEST) {
            //Repairing
            ToolTier broken = isHoldingBrokenTool(player);
            if (broken != null) {
                long required = getCost(broken) / 10;
                if (HFTrackers.getPlayerTrackerFromPlayer(player).getStats().getGold() < required) return;
                ItemStack material = getUpgradeMaterial(broken);
                if (InventoryHelper.takeItemsInInventory(player, ITEM_STACK, material)) {
                    date = HFApi.calendar.getDate(player.worldObj).copy();
                    tool = player.getHeldItemMainhand().copy();
                    tool.getSubCompound("Data", true).setInteger("Damage", 0);
                    tool.getSubCompound("Data", true).setDouble("Level", tool.getSubCompound("Data", true).getDouble("Level"));
                    days = 1; //Takes 1 day to repair
                    increaseStage(player);
                    rewardGold(player, -required);
                    takeHeldStack(player, 1);
                }

                return;
            }

            //Upgrading
            ToolTier holding = isHolding(player);
            if (holding != null) {
                if (getLevel(player) != 100D) return;
                long required = getCost(holding);
                if (HFTrackers.getPlayerTrackerFromPlayer(player).getStats().getGold() < required) return;
                if(InventoryHelper.takeItemsInInventory(player, ITEM_STACK, new ItemStack(HFMining.MATERIALS, 1, getMaterial(holding)), getRequired(holding))) {
                    date = HFApi.calendar.getDate(player.worldObj).copy();
                    tool = player.getHeldItemMainhand().copy();
                    tool.setTagCompound(null);
                    tool.setItemDamage(tool.getItemDamage() + 1);
                    days = 3; //Takes three days
                    increaseStage(player);
                    rewardGold(player, -required);
                    takeHeldStack(player, 1);
                }
            }
        } else {
            CalendarDate today = HFApi.calendar.getDate(player.worldObj);
            if (getDifference(date, today) >= days) {
                complete(player);
                player.worldObj.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.NEUTRAL, 0.25F, 1F);
                for (int i = 0; i < 32; i++) {
                    player.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entity.posX + player.worldObj.rand.nextFloat() + player.worldObj.rand.nextFloat() - 1F, entity.posY + 0.25D + entity.worldObj.rand.nextFloat() + entity.worldObj.rand.nextFloat(), entity.posZ + player.worldObj.rand.nextFloat() + player.worldObj.rand.nextFloat() - 1F, 0, 0, 0);
                }
            }
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
            days = nbt.getByte("Days");
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
            nbt.setByte("Days", (byte) days);
        }

        return nbt;
    }

    private boolean isRepairable(ItemStack stack, ToolTier tier) {
        if (tier == ToolTier.CURSED || tier == ToolTier.BLESSED) return false;
        return HFTools.HAMMER.getDamageForDisplay(stack) != 0;
    }

    private ToolTier isHoldingBrokenTool(EntityPlayer player) {
        ItemStack held = player.getHeldItemMainhand();
        if (held != null) {
            if (held.getItem() instanceof ItemTool) {
                ItemTool tool = ((ItemTool)held.getItem());
                ToolTier tier = tool.getTier(held);
                if (tool == HFTools.WATERING_CAN) return null;
                return isRepairable(held, tier) ? tier : null;
            }
        }

        return null;
    }

    private ToolTier isHolding(EntityPlayer player) {
        ItemStack held = player.getHeldItemMainhand();
        if (held != null) {
            if (held.getItem() instanceof ItemTool) {
                ItemTool tool = ((ItemTool)held.getItem());
                ToolTier tier = tool.getTier(held);
                return getRequired(tier) > 0 ? tier : null;
            }
        }

        return null;
    }
}

package joshie.harvest.quests.player.trade;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.InventoryHelper.SearchType;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestTrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
import java.util.Set;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.helpers.InventoryHelper.SPECIAL;

@HFQuest("trade.seeds")
public class QuestFlowerTrader extends QuestTrade {
    private final Random rand = new Random();
    private CalendarDate date;
    private int received = 3;

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JADE_MEET) && !finished.contains(Quests.JENNI_MEET);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        return entity.getNPC() == HFNPCs.FLOWER_GIRL && InventoryHelper.getHandItemIsIn(player, SPECIAL, SearchType.FLOWER, 5) != null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        CalendarDate today = HFApi.calendar.getDate(player.world);
        int days = date == null ? 5 : CalendarHelper.getDays(date, today);
        if (received > 0 && days >= 5) received = 0;
        if (received < 10) {
            Season season = HFApi.calendar.getDate(player.world).getSeason();
            if (season == SPRING || season == SUMMER || season == AUTUMN) {
                //Jade informs the player that she will happily trade flowers
                //For a bag of seeds
                int chance = player.world.rand.nextInt(3);
                return chance == 0 ? getLocalized("complete.yes1"): chance == 1 ? getLocalized("complete.yes2") : getLocalized("complete.yes3");
                //Jade Informs the player that she doesn't have in stock this time of year
            } else return getLocalized("complete.no");
        } else return getLocalized("return", 5 - days);
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        CalendarDate today = HFApi.calendar.getDate(player.world);
        int days = date == null ? 5 : CalendarHelper.getDays(date, today);
        if (date != null && received > 0 && days >= 5) {
            date = null;
            received = 0;
            increaseStage(player);
        }

        for (int i = 0; i < (wasSneaking ? 10: 1); i++) {
            if (received < 10) {
                if (InventoryHelper.takeItemsIfHeld(player, SPECIAL, SearchType.FLOWER, 5) != null) {
                    Season season = HFApi.calendar.getDate(player.world).getSeason();
                    if (season == SPRING || season == SUMMER || season == AUTUMN) {
                        rand.setSeed(today.hashCode());
                        if (rand.nextInt(30) == 0) rewardItem(player, HFCrops.TUTORIAL.getSeedStack(1));
                        else if (season == SPRING) rewardItem(player, HFCrops.TURNIP.getSeedStack(1));
                        else if (season == SUMMER) rewardItem(player, HFCrops.ONION.getSeedStack(1));
                        else rewardItem(player, HFCrops.SPINACH.getSeedStack(1));
                        if (!player.world.isRemote) {
                            received++; //Increase the amount we've received
                            if (received >= 10) {
                                received = 10; //Max it out
                                date = today.copy(); //Update when we got our last seeds
                                //Called in order to sync the data
                                increaseStage(player);
                                break;
                            }

                            //Called in order to sync the data
                            increaseStage(player);
                        }
                    }
                }
            }
        }

        //Force complete this, if we've met jenni
        if (HFApi.quests.hasCompleted(Quests.JENNI_MEET, player)) {
            complete(player);
        }
}

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Date")) {
            date = CalendarDate.fromNBT(nbt.getCompoundTag("Date"));
            received = nbt.getByte("Received");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (date != null) {
            nbt.setTag("Date", date.toNBT());
            nbt.setByte("Received", (byte) received);
        }

        return nbt;
    }
}

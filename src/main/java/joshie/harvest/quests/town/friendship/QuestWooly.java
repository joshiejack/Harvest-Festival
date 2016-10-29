package joshie.harvest.quests.town.friendship;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.base.QuestUnlockedTimer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static joshie.harvest.core.helpers.InventoryHelper.SPECIAL;
import static joshie.harvest.core.helpers.InventoryHelper.SearchType.WOOL;

@HFQuest("friendship.wooly")
public class QuestWooly extends QuestUnlockedTimer {
    private Result success = Result.DEFAULT;
    private CalendarDate requested;

    public QuestWooly() {
        setNPCs(HFNPCs.FLOWER_GIRL);
        setTownQuest();
    }

    @Override
    protected int getDaysBetween() {
        return 7;
    }

    @Override
    public boolean canUnlock(EntityPlayer player, EntityLiving entity, INPC npc) {
        return HFApi.calendar.getWeather(player.worldObj).isSnow() && super.canUnlock(player, entity, npc);
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (requested == null) return "Damn it's been pretty cold lately, I could really use some new clothes, do you think you have any wool you can spare?";
        else {
            int days = CalendarHelper.getDays(requested, HFApi.calendar.getDate(player.worldObj));
            if (days < getDaysBetween()) {
                if (InventoryHelper.takeItemsIfHeld(player, SPECIAL, WOOL) != null) {
                    return "Woah you are amazing, thanks for getting me this wool, now I can wrap up nice and warm for winter.";
                } else if (player.worldObj.rand.nextFloat() < 0.05F) return "I'd love some wool so I can make some new clothes for winter";
            } else {
                return "Are you sure you actually care about me? You've taken way too long, I've got my own clothes now!";
            }
        }

        return null;

    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean wasSneaking) {
        if (requested == null) requested = HFApi.calendar.getDate(player.worldObj).copy();
        else {
            int days = CalendarHelper.getDays(requested, HFApi.calendar.getDate(player.worldObj));
            if (days < getDaysBetween()) {
                if (InventoryHelper.takeItemsIfHeld(player,SPECIAL, WOOL) != null) {
                    success = Result.ALLOW;
                    complete(player);
                }
            } else {
                success = Result.DENY;
                complete(player);
            }
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        super.onQuestCompleted(player); //Call the super
        if (success == Result.ALLOW) HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.FLOWER_GIRL.getUUID(), 500);
         else if (success == Result.DENY) HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.FLOWER_GIRL.getUUID(), -100);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("RequestedDate")) {
            requested = CalendarDate.fromNBT(nbt.getCompoundTag("RequestedDate"));
            success = Result.valueOf(nbt.getString("Success"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (requested != null) {
            nbt.setTag("RequestedDate", requested.toNBT());
            nbt.setString("Success", success.name());
        }

        return nbt;
    }
}

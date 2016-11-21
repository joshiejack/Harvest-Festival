package joshie.harvest.quests.town.trade;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestTown;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

public class QuestFreeCake extends QuestTown  {
    private CalendarDate date;

    public QuestFreeCake() {
        setNPCs(HFNPCs.CAFE_GRANNY);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.BUILDING_CAFE);
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean wasSneaking) {
        CalendarDate today = HFApi.calendar.getDate(player.worldObj);
        if (today.getWeekday() == Weekday.SUNDAY) {
            if (date == null || CalendarHelper.getDays(date, today) >= 7) {

            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Date")) {
            date = CalendarDate.fromNBT(nbt.getCompoundTag("Date"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (date != null) {
            nbt.setTag("Date", date.toNBT());
        }

        return nbt;
    }
}

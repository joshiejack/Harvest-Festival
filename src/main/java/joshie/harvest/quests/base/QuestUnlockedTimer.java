package joshie.harvest.quests.base;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.calendar.CalendarHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class QuestUnlockedTimer extends QuestUnlocked {
    private CalendarDate lastCompletion;

    protected abstract int getDaysBetween();

    @Override
    public boolean canUnlock(EntityPlayer player, EntityLiving entity, INPC npc) {
        return CalendarHelper.getDays(lastCompletion, HFApi.calendar.getDate(player.worldObj)) >= 7;
    }

    @Override
    public void updateData(EntityPlayer player) {
        lastCompletion = HFApi.calendar.getDate(player.worldObj).copy();
    }

    @Override
    public NBTTagCompound getPermenantData() {
        return lastCompletion == null ? null : lastCompletion.toNBT();
    }

    public void readPermenantData(NBTTagCompound tag) {
        if (tag.hasKey("WeekDay")) lastCompletion = CalendarDate.fromNBT(tag);
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }
}

package joshie.harvest.core.util.adapter;

import joshie.harvest.api.calendar.CalendarDate;
import net.minecraft.nbt.NBTTagCompound;

public class CalendarAdapter extends SerializeAdapter<CalendarDate> {
    @Override
    public void writeToNBT(CalendarDate object, NBTTagCompound tag) {
        tag.setTag("Date", object.toNBT());
    }

    @Override
    public CalendarDate readFromNBT(NBTTagCompound tag) {
        return CalendarDate.fromNBT(tag.getCompoundTag("Date"));
    }
}

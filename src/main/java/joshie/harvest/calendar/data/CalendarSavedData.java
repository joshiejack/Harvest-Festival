package joshie.harvest.calendar.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class CalendarSavedData extends WorldSavedData {
    private final CalendarServer server = new CalendarServer();

    public CalendarSavedData(String string) {
        super(string);
    }

    public CalendarServer getCalendar() {
        return server;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        server.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return server.writeToNBT(nbt);
    }
}

package joshie.harvest.calendar;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class CalendarData extends WorldSavedData {
    private final CalendarServer server = new CalendarServer();

    public CalendarData(String string) {
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

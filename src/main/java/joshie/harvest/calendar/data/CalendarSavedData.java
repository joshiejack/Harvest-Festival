package joshie.harvest.calendar.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import javax.annotation.Nonnull;

public class CalendarSavedData extends WorldSavedData {
    private final CalendarServer server = new CalendarServer();

    public CalendarSavedData(String string) {
        super(string);
    }

    public CalendarServer getCalendar() {
        return server;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        server.readFromNBT(nbt);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        return server.writeToNBT(nbt);
    }
}

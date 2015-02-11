package joshie.harvestmoon.player;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.helpers.CalendarHelper;
import joshie.harvestmoon.util.IData;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerStats implements IData {
    private CalendarDate birthday = new CalendarDate();
    private double stamina = 1000;
    private double fatigue;
    private int gold;

    public PlayerDataServer master;
    public PlayerStats(PlayerDataServer master) {
        this.master = master;
    }

    public void newDay() {
        this.stamina = 1000;
        this.fatigue = 0;
    }

    public CalendarDate getBirthday() {
        return birthday;
    }

    public void affectStats(double stamina, double fatigue) {
        this.stamina += stamina;
        this.fatigue += fatigue;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public boolean setBirthday() {
        if (!birthday.isSet()) {
            birthday = new CalendarDate(CalendarHelper.getServerDate());
            return true;
        } else return false;
    }

    public int getGold() {
        return gold;
    }

    public double getStamina() {
        return stamina;
    }

    public double getFatigue() {
        return fatigue;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        birthday.readFromNBT(nbt);
        stamina = nbt.getDouble("Stamina");
        fatigue = nbt.getDouble("Fatigue");
        gold = nbt.getInteger("Gold");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        birthday.writeToNBT(nbt);
        nbt.setDouble("Stamina", stamina);
        nbt.setDouble("Fatigue", fatigue);
    }
}

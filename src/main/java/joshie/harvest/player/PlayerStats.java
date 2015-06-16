package joshie.harvest.player;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.core.util.IData;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerStats implements IData {
    private ICalendarDate birthday = HFApi.CALENDAR.newDate(0, null, 0);
    private double staminaMax = 100D;
    private double fatigueMin = 0D;
    private double stamina = 100D;
    private double fatigue = 0D;
    private long gold;

    public ICalendarDate getBirthday() {
        return birthday;
    }

    public void affectStats(double stamina, double fatigue) {
        this.stamina += stamina;
        this.fatigue += fatigue;

        if (this.stamina >= staminaMax) {
            this.stamina = staminaMax;
        }

        if (this.fatigue <= fatigueMin) {
            this.fatigue = fatigueMin;
        }
    }

    public void addGold(long gold) {
        this.gold += gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public boolean isBirthdaySet() {
        return birthday.getSeason() != null && birthday.getDay() != 0 && birthday.getYear() != 0;
    }

    public boolean setBirthday() {
        if (!isBirthdaySet()) {
            birthday = HFApi.CALENDAR.cloneDate(HFApi.CALENDAR.getToday());
            return true;
        } else return false;
    }
    
    public void setBirthday(ICalendarDate newDate) {
        birthday = newDate;
    }

    public void setStats(double stamina, double fatigue, double staminaMax, double fatigueMin) {
        this.stamina = stamina;
        this.fatigue = fatigue;
        this.staminaMax = staminaMax;
        this.fatigueMin = fatigueMin;   
    }

    public long getGold() {
        return gold;
    }

    public double getStamina() {
        return stamina;
    }

    public double getFatigue() {
        return fatigue;
    }

    public double getStaminaMax() {
        return staminaMax;
    }

    public double getFatigueMin() {
        return fatigueMin;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        birthday.readFromNBT(nbt);
        stamina = nbt.getDouble("Stamina");
        fatigue = nbt.getDouble("Fatigue");
        gold = nbt.getLong("Gold");
        staminaMax = nbt.getDouble("StaminaMax");
        fatigueMin = nbt.getDouble("FatigueMin");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        birthday.writeToNBT(nbt);
        nbt.setDouble("Stamina", stamina);
        nbt.setDouble("Fatigue", fatigue);
        nbt.setLong("Gold", gold);
        nbt.setDouble("StaminaMax", staminaMax);
        nbt.setDouble("FatigueMin", fatigueMin);
    }
}

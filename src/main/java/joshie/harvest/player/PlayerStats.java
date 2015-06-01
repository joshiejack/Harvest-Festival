package joshie.harvest.player;

import joshie.harvest.calendar.CalendarDate;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.util.IData;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerStats implements IData {
    private CalendarDate birthday = new CalendarDate();
    private double staminaMax = 100D;
    private double fatigueMin = 0D;
    private double stamina = 100D;
    private double fatigue = 0D;
    private long gold;
    
    public PlayerStats(PlayerDataServer master) {}

    public CalendarDate getBirthday() {
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

    public boolean setBirthday() {
        if (!birthday.isSet()) {
            birthday = new CalendarDate(CalendarHelper.getServerDate());
            return true;
        } else return false;
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

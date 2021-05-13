package uk.joshiejack.penguinlib.item.interfaces;

import net.minecraft.item.EnumAction;

public interface Edible {
    /** @return how much hunger this restores **/
    int getHunger();

    /** @return how much saturation this fills **/
    float getSaturation();

    /** @return how long this takes to consume **/
    default int getConsumeTime() {
        return 32;
    }

    /** @return the type of action, normally EAT or DRINK **/
    default EnumAction getAction() {
        return EnumAction.EAT;
    }
}

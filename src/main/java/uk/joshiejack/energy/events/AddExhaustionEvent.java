package uk.joshiejack.energy.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class AddExhaustionEvent extends PlayerEvent {
    private final float exhaustion;
    private float newValue;

    public AddExhaustionEvent(EntityPlayer player, float exhaustion) {
        super(player);
        this.exhaustion = exhaustion;
        this.newValue = exhaustion;
    }

    public float getExhaustion() {
        return exhaustion;
    }

    public float getNewValue() {
        return newValue;
    }

    public void setNewValue(float newValue) {
        this.newValue = newValue;
    }
}


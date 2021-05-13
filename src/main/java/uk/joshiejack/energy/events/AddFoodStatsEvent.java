package uk.joshiejack.energy.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class AddFoodStatsEvent extends PlayerEvent {
    private final ItemStack stack;
    private final int heal;
    private final float saturation;
    private int newHealth;
    private float newSaturation;

    public AddFoodStatsEvent(EntityPlayer player, ItemStack stack, int healAmount, float saturationModifier) {
        super(player);
        this.stack = stack;
        this.heal = healAmount;
        this.newHealth = healAmount;
        this.saturation = saturationModifier;
        this.newSaturation = saturationModifier;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getHeal() {
        return heal;
    }

    public float getSaturation() {
        return saturation;
    }

    public int getNewHealth() {
        return newHealth;
    }

    public float getNewSaturation() {
        return newSaturation;
    }

    public void setNewHealth(int newHealth) {
        this.newHealth = newHealth;
    }

    public void setNewSaturation(float newSaturation) {
        this.newSaturation = newSaturation;
    }
}


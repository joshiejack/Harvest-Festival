package joshie.harvest.npc;

import joshie.harvest.api.npc.IConditionalGreeting;
import net.minecraft.entity.player.EntityPlayer;

public class GreetingGeneric implements IConditionalGreeting {
    public String text;

    public GreetingGeneric(String text) {
        this.text = text;
    }

    @Override
    public boolean canDisplay(EntityPlayer player) {
        return true;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}

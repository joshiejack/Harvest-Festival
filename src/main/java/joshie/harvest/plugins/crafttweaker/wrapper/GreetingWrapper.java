package joshie.harvest.plugins.crafttweaker.wrapper;

import joshie.harvest.api.npc.IGreeting;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

public class GreetingWrapper implements IGreeting {
    private final String text;

    public GreetingWrapper(String text) {
        this.text = text;
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return text;
    }
}

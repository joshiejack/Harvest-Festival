package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.api.npc.IGreeting;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

public class GreetingShopTime implements IGreeting {
    private final String text;

    public GreetingShopTime(String text) {
        this.text = text;
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return text;
    }
}

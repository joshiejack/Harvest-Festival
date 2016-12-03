package joshie.harvest.plugins.crafttweaker.wrappers;

import joshie.harvest.api.npc.IGreeting;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

public class GreetingShopWrapper implements IGreeting {
    private final String text;

    public GreetingShopWrapper(String text) {
        this.text = text;
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return text;
    }
}

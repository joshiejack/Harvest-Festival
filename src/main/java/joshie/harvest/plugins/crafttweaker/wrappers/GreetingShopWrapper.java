package joshie.harvest.plugins.crafttweaker.wrappers;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.GreetingShop;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

public class GreetingShopWrapper extends GreetingShop {
    public GreetingShopWrapper(String text) {
        super(text);
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        return text;
    }
}

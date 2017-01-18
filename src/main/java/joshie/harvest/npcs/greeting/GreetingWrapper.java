package joshie.harvest.npcs.greeting;

import joshie.harvest.api.npc.IInfoButton;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.IGreeting;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

public class GreetingWrapper implements IInfoButton {
    private final IGreeting greeting;

    public GreetingWrapper(IGreeting greeting) {
        this.greeting = greeting;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        return greeting.getLocalizedText(player, ageable, npc);
    }
}

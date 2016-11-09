package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IGreeting;
import joshie.harvest.api.npc.IInfoButton;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

public class GreetingWrapper implements IInfoButton {
    private final IGreeting greeting;

    public GreetingWrapper(IGreeting greeting) {
        this.greeting = greeting;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return greeting.getLocalizedText(player, ageable, npc);
    }
}

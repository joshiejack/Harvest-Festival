package joshie.harvest.api.npc;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/** Called when npcs are being built, and after they are registered
 *  You can use this even to manipulate npcs,
 *  It's main purpose is to allow you to edit it's greetings though**/
public class NPCBuildEvent extends Event {
    private final List<IConditionalGreeting> greetings;
    private final INPC npc;

    public NPCBuildEvent(INPC npc, List<IConditionalGreeting> greetings) {
        this.greetings = greetings;
        this.npc = npc;
    }

    public List<IConditionalGreeting> getGreetings() {
        return greetings;
    }

    public INPC getNPC() {
        return npc;
    }
}

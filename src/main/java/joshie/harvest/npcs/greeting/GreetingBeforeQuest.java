package joshie.harvest.npcs.greeting;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

public class GreetingBeforeQuest extends GreetingLocalized {
    private final Quest required;
    private final Quest cannot;

    public GreetingBeforeQuest(String text, Quest required, Quest cannot) {
        super(text);
        this.required = required;
        this.cannot = cannot;
    }

    @Override
    public boolean canDisplay(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        return HFApi.quests.hasCompleted(required, player) && !HFApi.quests.hasCompleted(cannot, player);
    }
}

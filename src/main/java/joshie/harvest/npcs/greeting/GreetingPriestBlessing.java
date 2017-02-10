package joshie.harvest.npcs.greeting;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

public class GreetingPriestBlessing extends GreetingLocalized {
    public GreetingPriestBlessing() {
        super("harvestfestival.quest.trade.bless.reminder");
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        return I18n.translateToLocalFormatted(text, HFApi.quests.hasCompleted(Quests.TOMAS_15K, player) ? 1000 : 2500);
    }
}

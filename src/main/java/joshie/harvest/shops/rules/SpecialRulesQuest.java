package joshie.harvest.shops.rules;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SpecialRulesQuest implements ISpecialRules<EntityPlayer> {
    private final String unlocalised;
    private Quest quest;

    public SpecialRulesQuest(String unlocalised) {
        this.unlocalised = "seeds." + unlocalised;
    }

    public SpecialRulesQuest(Quest quest) {
        this.unlocalised = quest.getRegistryName().getResourceDomain();
        this.quest = quest;
    }

    public Quest getQuest() {
        if (quest == null) {
            quest = QuestHelper.getQuest(unlocalised);
        }

        return quest;
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return HFApi.quests.hasCompleted(getQuest(), player);
    }
}

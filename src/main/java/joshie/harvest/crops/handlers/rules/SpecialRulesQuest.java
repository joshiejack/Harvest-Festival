package joshie.harvest.crops.handlers.rules;

import joshie.harvest.api.buildings.ISpecialPurchaseRules;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.QuestHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpecialRulesQuest implements ISpecialPurchaseRules {
    private final String unlocalised;
    private Quest quest;

    public SpecialRulesQuest(String unlocalised) {
        this.unlocalised = "seeds." + unlocalised;
    }

    public Quest getQuest() {
        if (quest == null) {
            quest = QuestHelper.getQuest(unlocalised);
        }

        return quest;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return TownHelper.getClosestTownToEntity(player).getQuests().getFinished().contains(getQuest());
    }
}

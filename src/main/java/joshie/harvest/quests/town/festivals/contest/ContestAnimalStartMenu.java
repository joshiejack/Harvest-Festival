package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.quests.base.QuestAnimalContest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ContestAnimalStartMenu extends Selection<QuestAnimalContest> {
    public ContestAnimalStartMenu(String prefix) {
        super("harvestfestival.quest.festival." + prefix + ".start", "harvestfestival.quest.festival." + prefix + ".yes",
                "harvestfestival.quest.festival." + prefix + ".change", "harvestfestival.quest.festival." + prefix + ".no");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result onSelected(EntityPlayer player, NPCEntity entity, QuestAnimalContest quest, int option) {
        if (option == 1) {
            quest.targetEntries(player, entity);
            quest.getEntries().startContest(player); //Spawn any relevant data
            quest.setStage(QuestAnimalContest.START); //Mark as having started
            quest.syncData(player); //Sync up to the client
            quest.execute(entity.getTown(), player, entity); //Execute the pathing
            return Result.ALLOW;
        } else if (option == 2) {
            quest.getEntries().getSelecting().add(EntityHelper.getPlayerUUID(player));
            quest.syncData(player);
            return Result.ALLOW;
        } else return Result.DENY;
    }
}

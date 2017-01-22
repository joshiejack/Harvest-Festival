package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("friendship.abii.town")
public class QuestAbii15KHappyTown extends QuestFriendship {
    public QuestAbii15KHappyTown() {
        super(HFNPCs.DAUGHTER_CHILD, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.ABI_10K);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        for (NPC npc: NPC.REGISTRY) {
            if (player.worldObj.rand.nextInt(3) <= 1) {
                HFApi.player.getRelationsForPlayer(player).affectRelationship(RelationshipType.NPC, npc.getUUID(), 1000);
            }
        }
    }
}

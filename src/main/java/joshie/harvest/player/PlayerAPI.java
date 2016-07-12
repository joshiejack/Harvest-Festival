package joshie.harvest.player;

import joshie.harvest.api.player.IPlayerHelper;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.player.relationships.RelationshipHelper;
import joshie.harvest.quests.Quest;
import joshie.harvest.quests.packets.PacketQuestSetStage;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.harvest.core.network.PacketHandler.sendToClient;
import static joshie.harvest.core.network.PacketHandler.sendToServer;

public class PlayerAPI implements IPlayerHelper {
    private final IRelationships relations = new RelationshipHelper();

    @Override
    public IRelationships getRelationshipHelper() {
        return relations;
    }

    @Override
    public void syncQuest(Quest quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            sendToClient(new PacketQuestSetStage(quest, quest.getStage()), player);
        } else {
            sendToServer(new PacketQuestSetStage(quest, quest.getStage()));
        }
    }
}

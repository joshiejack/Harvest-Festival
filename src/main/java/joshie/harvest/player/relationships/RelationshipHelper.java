package joshie.harvest.player.relationships;

import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

@HFApiImplementation
public class RelationshipHelper implements IRelationships {
    public static final RelationshipHelper INSTANCE = new RelationshipHelper();
    private final HashMap<String, IRelatableDataHandler> dataHandlers = new HashMap<>();

    private RelationshipHelper() {
        registerDataHandler(new RelationshipHandlerEntity());
        registerDataHandler(new RelationshipHandlerNPC());
    }

    @Override
    public void registerDataHandler(IRelatableDataHandler handler) {
        dataHandlers.put(handler.name(), handler);
    }

    @Override
    public IRelatableDataHandler getDataHandler(String handler) {
        return dataHandlers.get(handler);
    }

    @Override
    public void adjustRelationship(EntityPlayer player, IRelatable relatable, int amount) {
        HFTrackers.getPlayerTracker(player).getRelationships().affectRelationship(player, relatable, amount);
    }

    @Override
    public int getRelationship(EntityPlayer player, IRelatable relatable) {
        return HFTrackers.getPlayerTracker(player).getRelationships().getRelationship(relatable);
    }

    @Override
    public int getMaximumRelationshipValue() {
        return HFNPCs.MAX_FRIENDSHIP;
    }
}

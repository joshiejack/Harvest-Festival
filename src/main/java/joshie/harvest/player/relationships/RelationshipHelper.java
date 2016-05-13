package joshie.harvest.player.relationships;

import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.core.config.NPC;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class RelationshipHelper implements IRelationships {
    private final HashMap<String, IRelatableDataHandler> dataHandlers = new HashMap<>();

    public RelationshipHelper() {
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
    public void talkTo(EntityPlayer player, IRelatable relatable) {
        HFTrackers.getPlayerTracker(player).getRelationships().talkTo(player, relatable);
    }

    @Override
    public void adjustRelationship(EntityPlayer player, IRelatable relatable, int amount) {
        HFTrackers.getPlayerTracker(player).getRelationships().affectRelationship(player, relatable, amount);
    }

    @Override
    public int getAdjustedRelationshipValue(EntityPlayer player, IRelatable relatable) {
        return HFTrackers.getPlayerTracker(player).getRelationships().getRelationship(relatable);
    }

    @Override
    public int getMaximumRelationshipValue() {
        return NPC.MAXIMUM_FRIENDSHIP;
    }
}

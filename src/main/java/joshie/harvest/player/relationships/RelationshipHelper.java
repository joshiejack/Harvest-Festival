package joshie.harvest.player.relationships;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class RelationshipHelper implements IRelationships {
    private static final HashMap<String, IRelatableDataHandler> dataHandlers = new HashMap<String, IRelatableDataHandler>();
    public static final int ADJUSTED_MAX = 65535;
    private static boolean isInit = false;

    @Override
    public void registerDataHandler(IRelatableDataHandler handler) {
        dataHandlers.put(handler.name(), handler);
    }

    public static IRelatableDataHandler getHandler(String handler) {
        if (!isInit) {
            isInit = true;
            init();
        }
        
        return dataHandlers.get(handler);
    }
    
    public static void init() {
        HFApi.RELATIONS.registerDataHandler(new RelationshipHandlerEntity());
        HFApi.RELATIONS.registerDataHandler(new RelationshipHandlerNPC());
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
        return 1 + Short.MAX_VALUE + getRealRelationshipValue(player, relatable);
    }
    
    @Override
    public short getRealRelationshipValue(EntityPlayer player, IRelatable relatable) {
        return HFTrackers.getPlayerTracker(player).getRelationships().getRelationship(relatable);
    }
}

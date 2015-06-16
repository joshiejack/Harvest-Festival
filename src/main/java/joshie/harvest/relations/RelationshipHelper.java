package joshie.harvest.relations;

import java.util.HashMap;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.core.handlers.HFTracker;
import joshie.harvest.relations.data.DataHandlerEntity;
import joshie.harvest.relations.data.DataHandlerNPC;
import net.minecraft.entity.player.EntityPlayer;

public class RelationshipHelper implements IRelationships {
    private static final HashMap<String, IRelatableDataHandler> dataHandlers = new HashMap();
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
        HFApi.RELATIONS.registerDataHandler(new DataHandlerEntity());
        HFApi.RELATIONS.registerDataHandler(new DataHandlerNPC());
    }

    @Override
    public void talkTo(EntityPlayer player, IRelatable relatable) {
        HFTracker.getPlayerTracker(player).getRelationships().talkTo(relatable);
    }

    @Override
    public void adjustRelationship(EntityPlayer player, IRelatable relatable, int amount) {
        HFTracker.getPlayerTracker(player).getRelationships().affectRelationship(relatable, amount);
    }

    @Override
    public int getAdjustedRelationshipValue(EntityPlayer player, IRelatable relatable) {
        return 1 + Short.MAX_VALUE + getRealRelationshipValue(player, relatable);
    }
    
    @Override
    public short getRealRelationshipValue(EntityPlayer player, IRelatable relatable) {
        return HFTracker.getPlayerTracker(player).getRelationships().getRelationship(relatable);
    }
}

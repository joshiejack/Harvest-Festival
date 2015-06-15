package joshie.harvest.relations;

import java.util.HashMap;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.relations.IDataHandler;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.relations.data.DataHandlerEntity;
import joshie.harvest.relations.data.DataHandlerNPC;
import net.minecraft.entity.player.EntityPlayer;

public class RelationshipHelper implements IRelationships {
    private static final HashMap<String, IDataHandler> dataHandlers = new HashMap();
    public static final int ADJUSTED_MAX = 65535;
    private static boolean isInit = false;

    @Override
    public void registerDataHandler(IDataHandler handler) {
        dataHandlers.put(handler.name(), handler);
    }

    public static IDataHandler getHandler(String handler) {
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
        HarvestFestival.instance.proxy.getPlayerTracker(player).getRelationships().talkTo(relatable);
    }

    @Override
    public int getAdjustedRelationshipValue(EntityPlayer player, IRelatable relatable) {
        return 1 + Short.MAX_VALUE + getRealRelationshipValue(player, relatable);
    }
    
    @Override
    public short getRealRelationshipValue(EntityPlayer player, IRelatable relatable) {
        return HarvestFestival.proxy.getPlayerTracker(player).getRelationships().getRelationship(relatable);
    }
}

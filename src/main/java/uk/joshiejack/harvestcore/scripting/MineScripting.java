package uk.joshiejack.harvestcore.scripting;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.mine.MineHelper;
import uk.joshiejack.harvestcore.world.mine.dimension.MineData;
import uk.joshiejack.harvestcore.world.mine.dimension.MineTeleporter;
import uk.joshiejack.harvestcore.world.storage.SavedData;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.EntityJS;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldJS;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class MineScripting {
    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("mine", MineScripting.class);
    }

    @SuppressWarnings("unchecked")
    public static void updateMaxFloor(EntityJS<?> entityW) {
        Entity entity = entityW.penguinScriptingObject;
        int id = MineHelper.getMineIDFromEntity(entity);
        int floor = MineHelper.getFloorFromEntity(entity);
        SavedData.getMineData(entity.world, entity.world.provider.getDimension()).updateMaxFloor(entity.world, id, floor);
    }

    @SuppressWarnings("unchecked")
    public static int floor(EntityJS<?> entityW) {
        return MineHelper.getFloorFromEntity(entityW.penguinScriptingObject);
    }

    @SuppressWarnings("unchecked")
    public static int id(EntityJS<?> entityW) {
        return MineHelper.getMineIDFromEntity(entityW.penguinScriptingObject);
    }

    @SuppressWarnings("unchecked")
    public static boolean isIn(EntityJS<?> entityW) {
        Entity entity = entityW.penguinScriptingObject;
        return Mine.BY_ID.containsKey(entity.world.provider.getDimension());
    }

    public static MineData get(WorldJS<?> worldJS,  String name) {
        return SavedData.getMineDataFromName(worldJS.penguinScriptingObject, name);
    }

    public static int id(String name) {
        return Mine.BY_NAME.getInt(name);
    }

    @SuppressWarnings("unchecked")
    public static void enter(EntityJS<?> entityW, int mine, int town, int floor) {
        Entity entity = entityW.penguinScriptingObject;
        if (entity.world.provider.getDimension() == mine)
            MineHelper.teleportToFloor(entity, Mine.BY_ID.get(mine), town, floor, true);
        else entity.changeDimension(mine, new MineTeleporter(Mine.BY_ID.get(mine), town));
    }

    @SuppressWarnings("unchecked")
    public static void exit(EntityJS<?> entityW) {
        Entity entity = entityW.penguinScriptingObject;
        MineHelper.teleportToOverworld(entity, MineHelper.getMineIDFromEntity(entity));
    }
}

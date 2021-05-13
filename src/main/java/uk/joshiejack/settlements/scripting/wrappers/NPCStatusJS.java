package uk.joshiejack.settlements.scripting.wrappers;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.settlements.AdventureData;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.npcs.status.StatusTracker;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldJS;

public class NPCStatusJS extends AbstractJS<ResourceLocation> {
    public NPCStatusJS(ResourceLocation npc) {
        super(npc);
    }

    public boolean is(WorldJS<?> worldJS, String status, int value) {
        ResourceLocation object = penguinScriptingObject;
        AdventureData data = AdventureDataLoader.get(worldJS.penguinScriptingObject);
        for (StatusTracker tracker: data.getStatusTrackers()) {
            if (tracker.get(object, status) == value) return true;
        }

        return false;
    }

    public int get(PlayerJS wrapper, String status) {
        return AdventureDataLoader.get(wrapper.penguinScriptingObject.world).getStatusTracker(wrapper.penguinScriptingObject).get(penguinScriptingObject, status);
    }

    public void set(PlayerJS wrapper, String status, int value) {
        AdventureData data = AdventureDataLoader.get(wrapper.penguinScriptingObject.world);
        data.getStatusTracker(wrapper.penguinScriptingObject).set(wrapper.world().penguinScriptingObject, penguinScriptingObject, status, value);
        data.markDirty();
    }

    public void adjust(PlayerJS wrapper, String status, int value) {
        AdventureData data = AdventureDataLoader.get(wrapper.penguinScriptingObject.world);
        data.getStatusTracker(wrapper.penguinScriptingObject)
                .adjust(wrapper.world().penguinScriptingObject, penguinScriptingObject, status, value);
        data.markDirty();
    }

    public void adjustWithRange(PlayerJS wrapper, String status, int value, int min, int max) {
        ResourceLocation object = penguinScriptingObject;
        AdventureData data = AdventureDataLoader.get(wrapper.penguinScriptingObject.world);
        StatusTracker tracker = data.getStatusTracker(wrapper.penguinScriptingObject);
        tracker.adjust(wrapper.world().penguinScriptingObject, object, status, value);
        tracker.set(wrapper.world().penguinScriptingObject, object, status, Math.min(max, Math.max(min, tracker.get(object, status))));
        data.markDirty();
    }
}

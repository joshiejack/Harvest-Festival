package uk.joshiejack.settlements.scripting.wrappers;

import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.settlements.quest.data.QuestData;
import uk.joshiejack.settlements.quest.data.QuestTracker;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class QuestTrackerJS extends AbstractJS<QuestTracker> {
    public QuestTrackerJS(QuestTracker tracker) {
        super(tracker);
    }

    public boolean completed(String quest) {
        return penguinScriptingObject.hasCompleted(new ResourceLocation(quest));
    }

    public boolean completed(String quest, int amount) {
        return penguinScriptingObject.hasCompleted(new ResourceLocation(quest), amount);
    }

    public QuestJS byName(String name) {
        return new QuestJS(Quest.REGISTRY.get(new ResourceLocation(name)));
    }

    public void complete(EntityPlayer player, String name) {
        AdventureDataLoader.get(player.world).markCompleted(player, Quest.REGISTRY.get(new ResourceLocation(name)));
    }

    public void call(String q, String function, Object... data) {
        Quest quest = Quest.REGISTRY.get(new ResourceLocation(q));
        if (quest != null) {
            QuestData storage = penguinScriptingObject.getData(quest.getRegistryName());
            if (storage != null) {
                storage.callFunction(function, data);
            }
        }
    }
}

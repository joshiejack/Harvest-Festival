package uk.joshiejack.settlements.scripting.wrappers;

import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("unused")
public class QuestJS extends AbstractJS<Quest> {
    public QuestJS(Quest quest) {
        super(quest);
    }

    public void complete(PlayerJS player) {
        AdventureDataLoader.get(player.penguinScriptingObject.world).markCompleted(player.penguinScriptingObject, penguinScriptingObject);
    }

    @SuppressWarnings("all")
    public String localize(String suffix) {
        ResourceLocation script = penguinScriptingObject.getRegistryName();
        String text = script.getNamespace() + ".quest." + script.getPath().replace("/", ".");
        return text + "." + suffix;
    }
}

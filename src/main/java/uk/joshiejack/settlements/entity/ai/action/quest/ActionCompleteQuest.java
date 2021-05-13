package uk.joshiejack.settlements.entity.ai.action.quest;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.entity.ai.action.registry.AbstractActionRegistry;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

@PenguinLoader("complete_quest")
public class ActionCompleteQuest extends AbstractActionRegistry<Quest> {
    public ActionCompleteQuest() {
        super(Quest.REGISTRY);
    }

    public Path getParent(Path path, int count) {
        for (int i = 0; i <= count; i++) {
            path = path.getParent();
        }

        return path;
    }

    @Override
    public ActionCompleteQuest withData(Object... params) {
        if (params.length == 0) {
            this.resource = registryName;
            return this;
        } else {
            String param = (String) params[0];
            if (param.contains(":")) { //Absolute Path
                return (ActionCompleteQuest) super.withData(params);
            } else { //Relative path to THIS quest
                /* ../ means go up a route level for this quest */
                // Location can be STRING or File
                String filepath = Quest.REGISTRY.get(registryName).getInterpreter().getLocation().toString();
                // This will be "harvestfestival:npcs/harvest_goddess/0/axe" //TODO: FILE VERSION
                // Converted to
                String name = getParent(Paths.get(filepath.split(":")[1]), StringUtils.countMatches(param, "../")).toString();
                this.resource = new ResourceLocation(name.replace("/", "_") + "_" + param);
            }
        }

        return this;
    }

    @Override
    public void performAction(World world, Quest object) {
        if (isQuest) {
            AdventureDataLoader.get(world).markCompleted(player, Quest.REGISTRY.get(resource));
        }
    }
}

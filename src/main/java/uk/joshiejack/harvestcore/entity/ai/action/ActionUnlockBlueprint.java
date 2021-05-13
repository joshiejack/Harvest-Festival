package uk.joshiejack.harvestcore.entity.ai.action;

import net.minecraft.world.World;
import uk.joshiejack.settlements.entity.ai.action.registry.AbstractActionRegistry;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader("unlock_blueprint")
public class ActionUnlockBlueprint extends AbstractActionRegistry<Blueprint> {
    public ActionUnlockBlueprint() {
        super(Blueprint.REGISTRY);
    }

    @Override
    public void performAction(World world, Blueprint blueprint) {
        blueprint.unlock(player);
    }
}

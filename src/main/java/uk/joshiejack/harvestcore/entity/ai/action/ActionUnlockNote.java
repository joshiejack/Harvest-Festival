package uk.joshiejack.harvestcore.entity.ai.action;

import net.minecraft.world.World;
import uk.joshiejack.settlements.entity.ai.action.registry.AbstractActionRegistry;
import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader("unlock_note")
public class ActionUnlockNote extends AbstractActionRegistry<Note> {
    public ActionUnlockNote() {
        super(Note.REGISTRY);
    }

    @Override
    public void performAction(World world, Note note) {
        note.unlock(player);
    }
}

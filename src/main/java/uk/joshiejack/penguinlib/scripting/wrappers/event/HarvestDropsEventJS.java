package uk.joshiejack.penguinlib.scripting.wrappers.event;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.BlockEvent;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.*;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;

public class HarvestDropsEventJS extends AbstractEventJS<BlockEvent.HarvestDropsEvent> {
    public HarvestDropsEventJS(BlockEvent.HarvestDropsEvent event) {
        super(event);
    }

    public int length() {
        return penguinScriptingObject.getDrops().size();
    }

    ItemStackJS item(int i) {
        return WrapperRegistry.wrap(penguinScriptingObject.getDrops().get(i));
    }

    public PlayerJS player() {
        BlockEvent.HarvestDropsEvent event = penguinScriptingObject;
        return WrapperRegistry.wrap(event.getHarvester() == null ? FakePlayerHelper.getFakePlayerWithPosition((WorldServer) event.getWorld(), event.getPos()) : event.getHarvester());
    }

    public WorldJS<?> world() {
        return WrapperRegistry.wrap(penguinScriptingObject.getWorld());
    }

    public PositionJS pos() {
        return WrapperRegistry.wrap(penguinScriptingObject.getPos());
    }

    public StateJS state() {
        return WrapperRegistry.wrap(penguinScriptingObject.getState());
    }

    public void add(ItemStackJS stackW) {
        penguinScriptingObject.getDrops().add(stackW.penguinScriptingObject);
    }

    public void remove(String holder) {
        Holder h = Holder.getFromString(holder);
        penguinScriptingObject.getDrops().removeIf(h::matches);
    }

    public void removeAll() {
        penguinScriptingObject.getDrops().clear();
    }
}

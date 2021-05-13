package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class EntityItemJS extends EntityJS<EntityItem> {
    public EntityItemJS(EntityItem entity) {
        super(entity);
    }

    public ItemStackJS item() {
        return WrapperRegistry.wrap(penguinScriptingObject.getItem());
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public PlayerJS thrower() {
        EntityItem object = penguinScriptingObject;
        String thrower = object.getThrower();
        if (thrower != null) {
            EntityPlayer player = object.world.getPlayerEntityByName(object.getThrower());
            return player == null ? null : WrapperRegistry.wrap(player);
        } else return null;
    }
}

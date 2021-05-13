package uk.joshiejack.penguinlib.scripting.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;

public class ScriptingTriggerFired extends Event {
    private final String method;
    private final Object[] objects;

    public ScriptingTriggerFired(String method, Object... objects) {
        this.method = method;
        this.objects = objects;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getObjects() {
        return objects;
    }

    @Nullable
    public EntityPlayer getPlayer() {
        return objects[0] instanceof EntityPlayer ? (EntityPlayer) objects[0] : null;
    }
}

package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;

import java.util.Objects;
import java.util.UUID;

public class WorldServerJS extends WorldJS<WorldServer> {
    public WorldServerJS(WorldServer world) {
        super(world);
    }

    public void displayParticle(EnumParticleTypes type, int number, double x, double y, double z, double speed) {
        penguinScriptingObject.spawnParticle(type, x, y, z, number, 0D, 0D, 0D, speed);
    }

    public EntityJS<?> getEntityByUUID(String uuid) {
        return WrapperRegistry.wrap(Objects.requireNonNull(penguinScriptingObject.getEntityFromUuid(UUID.fromString(uuid))));
    }
}

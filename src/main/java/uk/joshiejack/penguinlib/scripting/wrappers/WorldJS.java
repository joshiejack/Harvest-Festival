package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class WorldJS<W extends World> extends AbstractJS<W> {
    public WorldJS(W world) {
        super(world);
    }

    //EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed, int... particleArguments

    public BiomeJS biome(PositionJS pos) {
        return WrapperRegistry.wrap(penguinScriptingObject.getBiome(pos.penguinScriptingObject));
    }

    public void playSound(PositionJS pos, String sound, SoundCategory category, double volume, double pitch) {
        penguinScriptingObject.playSound(null, pos.penguinScriptingObject, Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation(sound))), category, (float) volume, (float) pitch);
    }

    @SuppressWarnings("ConstantConditions")
    public EntityJS<?> getEntity(String name, int x, int y, int z, int distance) {
        Class <? extends Entity> clazz = EntityList.getClass(new ResourceLocation(name));
        List<Entity> entity = penguinScriptingObject.getEntitiesWithinAABB(clazz, new AxisAlignedBB(x - 0.5F, y - 0.5F, z - 0.5F, x + 0.5F, y + 0.5F, z + 0.5F).expand(distance, distance, distance));
        return entity.size() > 0 ? WrapperRegistry.wrap(entity.get(0)) : null;
    }

    public AbstractJS<?> createEntity(String name, double x, double y, double z) {
        World object = penguinScriptingObject;
        Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(name), object);
        if (entity != null) {
            entity.setPosition(x, y, z);
            object.spawnEntity(entity);
            return WrapperRegistry.wrap(entity);
        } else return null;
    }

    public PlayerJS getPlayer(UUID uuid) {
        return WrapperRegistry.wrap(Objects.requireNonNull(PlayerHelper.getPlayerFromUUID(penguinScriptingObject, uuid)));
    }

    public boolean isAir(PositionJS wrapper) {
        return penguinScriptingObject.isAirBlock(wrapper.penguinScriptingObject);
    }

    public long time() {
        return penguinScriptingObject.getWorldTime();
    }

    public void drop(PositionJS pos, ItemStackJS wrapper) {
        Block.spawnAsEntity(penguinScriptingObject, pos.penguinScriptingObject, wrapper.penguinScriptingObject);
    }

    public int id() {
        return penguinScriptingObject.provider.getDimension();
    }

    public boolean isClient() {
        return penguinScriptingObject.isRemote;
    }

    public StateJS getState(PositionJS position) {
        return WrapperRegistry.wrap(penguinScriptingObject.getBlockState(position.penguinScriptingObject));
    }

    public void setState(StateJS block, PositionJS position) {
        World world = penguinScriptingObject;
        BlockPos pos = position.penguinScriptingObject;
        IBlockState state = block.penguinScriptingObject;
        world.setBlockState(pos, state, 2);
        world.notifyNeighborsOfStateChange(pos, state.getBlock(), false);
    }
}

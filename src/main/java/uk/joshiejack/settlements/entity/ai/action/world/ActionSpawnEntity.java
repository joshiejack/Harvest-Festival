package uk.joshiejack.settlements.entity.ai.action.world;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.penguinlib.scripting.wrappers.PositionJS;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

@PenguinLoader("spawn_entity")
public class ActionSpawnEntity extends ActionMental {
    private ResourceLocation entityName;
    private BlockPos pos;

    @Override
    public ActionSpawnEntity withData(Object... params) {
        entityName = new ResourceLocation((String) params[0]);
        if (params.length == 2) {
            pos = ((PositionJS)params[0]).penguinScriptingObject;
        } else pos = new BlockPos((int)params[1], (int)params[2], (int)params[3]);
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        Entity entity = EntityList.createEntityByIDFromName(entityName, player.world);
        if (entity != null) {
            entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            npc.world.spawnEntity(entity);
            return EnumActionResult.SUCCESS;
        } else return EnumActionResult.FAIL;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Entity", entityName.toString());
        tag.setLong("Position", pos.toLong());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        entityName = new ResourceLocation(tag.getString("Entity"));
        pos = BlockPos.fromLong(tag.getLong("Position"));
    }
}

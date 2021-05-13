package uk.joshiejack.settlements.entity.ai.action.move;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionPhysical;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;

@PenguinLoader("teleport")
public class ActionTeleport extends ActionPhysical {
    private BlockPos target;

    @Override
    public ActionTeleport withData(Object... params) {
        this.target = WrapperRegistry.unwrap(params[0]);
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        npc.setLocationAndAngles(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 0F, 0F);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("Target", target.toLong());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.target = BlockPos.fromLong(tag.getLong("Target"));
    }
}

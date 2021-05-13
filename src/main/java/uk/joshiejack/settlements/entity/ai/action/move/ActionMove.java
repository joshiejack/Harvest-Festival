package uk.joshiejack.settlements.entity.ai.action.move;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionPhysical;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;

@PenguinLoader("move")
public class ActionMove extends ActionPhysical {
    private BlockPos target;
    private double speed;

    @Override
    public ActionMove withData(Object... params) {
        this.target = WrapperRegistry.unwrap(params[0]);
        this.speed = (double) params[1];
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        npc.getNavigator().setPath(npc.getNavigator().getPathToXYZ(target.getX() + 0.5, target.getY(), target.getZ() + 0.5), speed);
        if (npc.getDistance(target.getX() + 0.5, target.getY(), target.getZ() + 0.5) < 1) {
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("Target", target.toLong());
        tag.setDouble("Speed", speed);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.target = BlockPos.fromLong(tag.getLong("Target"));
        this.speed = tag.getDouble("Speed");
    }
}

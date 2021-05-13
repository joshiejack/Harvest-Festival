package uk.joshiejack.settlements.entity.ai.action.move;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionPhysical;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;

@PenguinLoader("wait")
public class ActionWait extends ActionPhysical {
    private int ticks;
    private int targetTicks;

    @Override
    public ActionWait withData(Object... data) {
        this.targetTicks = (Integer) data[0] * 20;
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (ticks < targetTicks) {
            npc.getNavigator().clearPath();
            ticks++;
            return EnumActionResult.PASS;
        } else return EnumActionResult.SUCCESS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Target", targetTicks);
        tag.setInteger("Ticks", ticks);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.targetTicks = tag.getInteger("Target");
        this.ticks = tag.getInteger("Ticks");
    }
}

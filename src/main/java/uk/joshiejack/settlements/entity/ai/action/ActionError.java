package uk.joshiejack.settlements.entity.ai.action;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;

@PenguinLoader("error")
public class ActionError extends ActionMental {
    public static final Action INSTANCE = new ActionError();

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        return EnumActionResult.FAIL;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {}
}

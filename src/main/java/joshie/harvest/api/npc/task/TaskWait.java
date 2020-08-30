package joshie.harvest.api.npc.task;

import joshie.harvest.api.npc.NPCEntity;
import net.minecraft.nbt.NBTTagCompound;

@HFTask("wait")
public class TaskWait extends TaskElement {
    private int target;
    private int ticker;

    private TaskWait(int target) {
        this.target = target;
    }

    public static TaskWait of(int target) {
        return new TaskWait(target);
    }

    @Override
    public void execute(NPCEntity npc) {
        npc.getAsEntity().getNavigator().clearPath();
        ticker++; //Continue Executing
    }

    @Override
    public boolean isSatisfied(NPCEntity npc) {
        return ticker >= target;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        target = tag.getInteger("Target");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("Target", target);
        return tag;
    }
}

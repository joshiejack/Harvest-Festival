package joshie.harvest.api.npc.task;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;

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
    public void execute(EntityAgeable npc) {
        npc.getNavigator().clearPathEntity();
        ticker++; //Continue Executing
    }

    @Override
    public boolean isSatisfied(EntityAgeable npc) {
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

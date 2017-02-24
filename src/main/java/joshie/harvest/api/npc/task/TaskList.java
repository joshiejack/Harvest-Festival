package joshie.harvest.api.npc.task;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class TaskList {
    private final Queue<TaskElement> targets = new LinkedList<>();
    private TaskList() {}

    public static TaskList target(TaskElement... pos) {
        TaskList path = new TaskList();
        Collections.addAll(path.targets, pos);
        return path;
    }

    public TaskElement getCurrentTarget(EntityAgeable npc) {
        TaskElement target = targets.peek();
        if (target == null || target.isSatisfied(npc)) {
            return targets.poll();
        }

        return target;
    }

    public static TaskList fromNBT(NBTTagCompound tag) {
        TaskList path = new TaskList();
        NBTTagList tasks = tag.getTagList("Elements", 10);
        for (int i = 0; i < tasks.tagCount(); i++) {
            try {
                NBTTagCompound nbt = tasks.getCompoundTagAt(i);
                ResourceLocation resource = new ResourceLocation(nbt.getString("Resource"));
                TaskElement element = (TaskElement) TaskElement.REGISTRY.get(resource).newInstance();
                element.readFromNBT(nbt.getCompoundTag("Data"));
                path.targets.add(element);
            } catch (InstantiationException | IllegalAccessException ex) {/**/}
        }

        return path;
    }

    public NBTTagCompound toNBT(NBTTagCompound tag) {
        NBTTagList tasks = new NBTTagList();
        for (TaskElement element: targets) {
            NBTTagCompound nbt = new NBTTagCompound();
            ResourceLocation resource = TaskElement.REGISTRY.inverse().get(element.getClass());
            nbt.setString("Resource", resource.toString());
            nbt.setTag("Data", element.writeToNBT(new NBTTagCompound()));
            tasks.appendTag(nbt);
        }

        tag.setTag("Elements", tasks);
        return tag;
    }
}

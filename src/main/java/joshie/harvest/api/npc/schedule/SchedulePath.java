package joshie.harvest.api.npc.schedule;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class SchedulePath {
    private final Queue<ScheduleElement> targets = new LinkedList<>();
    private SchedulePath() {}

    public static SchedulePath target(ScheduleElement... pos) {
        SchedulePath path = new SchedulePath();
        Collections.addAll(path.targets, pos);
        return path;
    }

    public ScheduleElement getCurrentTarget(EntityAgeable npc) {
        ScheduleElement target = targets.peek();
        if (target == null || target.isSatisfied(npc)) {
            return targets.poll();
        }

        return target;
    }

    public static SchedulePath fromNBT(NBTTagCompound tag) {
        SchedulePath path = new SchedulePath();
        NBTTagList tasks = tag.getTagList("Elements", 10);
        for (int i = 0; i < tasks.tagCount(); i++) {
            try {
                NBTTagCompound nbt = tasks.getCompoundTagAt(i);
                ResourceLocation resource = new ResourceLocation(nbt.getString("Resource"));
                ScheduleElement element = (ScheduleElement) ScheduleElement.REGISTRY.get(resource).newInstance();
                element.readFromNBT(nbt.getCompoundTag("Data"));
                path.targets.add(element);
            } catch (InstantiationException | IllegalAccessException ex) {/**/}
        }

        return path;
    }

    public NBTTagCompound toNBT(NBTTagCompound tag) {
        NBTTagList tasks = new NBTTagList();
        for (ScheduleElement element: targets) {
            NBTTagCompound nbt = new NBTTagCompound();
            ResourceLocation resource = ScheduleElement.REGISTRY.inverse().get(element.getClass());
            nbt.setString("Resource", resource.toString());
            nbt.setTag("Data", element.writeToNBT(new NBTTagCompound()));
            tasks.appendTag(nbt);
        }

        tag.setTag("Elements", tasks);
        return tag;
    }
}

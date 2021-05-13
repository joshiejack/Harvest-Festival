package uk.joshiejack.settlements.entity.ai;

import com.google.common.collect.Lists;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.Action;

import java.util.Deque;
import java.util.LinkedList;

public class EntityAIActionQueue extends EntityAIBase implements INBTSerializable<NBTTagList> {
    private final Deque<Action> queue = new LinkedList<>();
    private final EntityNPC npc;
    private Action current;

    public EntityAIActionQueue(EntityNPC npc, int mutex) {
        this.npc = npc;
        this.setMutexBits(mutex);
    }

    public Deque<Action> all() {
        return queue;
    }

    public Action getCurrent() {
        return current;
    }

    public void addToEnd(Action action) {
        queue.addLast(action);
    }

    public void addToEnd(LinkedList<Action> object) {
        object.forEach(queue::addLast); //Add to the end?
    }

    public void addToHead(Action action) {
        queue.addFirst(action);
    }

    public void addToHead(LinkedList<Action> object) {
       Lists.reverse(object).forEach(queue::addFirst); //Reverse the list to add it in the correct order at the head of the queue
        current = null; //Clear out the head of the queue
    }

    @Override
    public boolean shouldExecute() {
        return current != null || !queue.isEmpty();
    }

    @Override
    public void updateTask() {
        if (current == null) current = queue.peek();
        if (current != null) {
            EnumActionResult result = current.execute(npc); //Execute the task
            if (result == EnumActionResult.FAIL) queue.clear();
            else if (result == EnumActionResult.SUCCESS) {
                queue.remove(); //Remove the head of the queue
                current = queue.peek(); //Grab the new head of the queue
            }
        }
    }

    @Override
    public void startExecuting() {
        npc.getNavigator().clearPath();
    }

    @Override
    public NBTTagList serializeNBT() {
        NBTTagList list = new NBTTagList();
        queue.forEach(action -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Type", action.getType());
            tag.setTag("Data", action.serializeNBT());
            list.appendTag(tag);
        });

        return list;
    }

    @Override
    public void deserializeNBT(NBTTagList list) {
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            Action action = Action.createOfType(tag.getString("Type"));
            action.deserializeNBT(tag.getCompoundTag("Data"));
            queue.add(action); //This might help ;)
        }
    }
}

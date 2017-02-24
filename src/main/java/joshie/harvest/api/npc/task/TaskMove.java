package joshie.harvest.api.npc.task;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class TaskMove extends TaskElement {
    private BlockPos pos;

    private TaskMove(BlockPos pos) {
        this.pos = pos;
    }

    public static TaskMove of(BlockPos pos) {
        return new TaskMove(pos);
    }

    public TaskElement offset(EnumFacing facing, int amount) {
        return TaskMove.of(pos.offset(facing, amount));
    }

    @Nullable
    private Path getPathToTarget(EntityAgeable npc) {
        Path path = npc.getNavigator().getPathToPos(pos);
        if (path != null) return path;
        else {
            //Grab a random block towards the target
            Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(npc, 10, 7, new Vec3d((double) pos.getX() + 0.5D, (double) pos.getY() + 1D, (double) pos.getZ() + 0.5D));
            if (vec != null) {
                return npc.getNavigator().getPathToPos(new BlockPos(vec));
            } else return null;
        }
    }

    @Override
    public void execute(EntityAgeable npc) {
        Path path = getPathToTarget(npc);
        if (path != null) {
            npc.getNavigator().setPath(path, 0.6F);
        }
    }

    @Override
    public boolean isSatisfied(EntityAgeable npc) {
        return npc.getDistanceSq(pos) <= 1D;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        pos = BlockPos.fromLong(tag.getLong("Pos"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setLong("Pos", pos.toLong());
        return tag;
    }
}

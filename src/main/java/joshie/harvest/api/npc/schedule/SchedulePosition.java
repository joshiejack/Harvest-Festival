package joshie.harvest.api.npc.schedule;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SchedulePosition extends ScheduleElement<BlockPos> {
    private final BlockPos pos;

    private SchedulePosition(BlockPos pos) {
        this.pos = pos;
    }

    public static SchedulePosition of(BlockPos pos) {
        return new SchedulePosition(pos);
    }

    public ScheduleElement offset(EnumFacing facing, int amount) {
        return SchedulePosition.of(pos.offset(facing, amount));
    }

    @Override
    public void execute(EntityAgeable npc) {
        Path path = npc.getNavigator().getPathToPos(pos);
        if (path == null) {
            Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(npc, 3, 5, new Vec3d((double) pos.getX() + 0.5D, (double) pos.getY() + 1D, (double) pos.getZ() + 0.5D));
            if (vec != null) {
                path = npc.getNavigator().getPathToPos(new BlockPos(vec));
            }
        }

        npc.getNavigator().setPath(path, 0.6F);
    }

    @Override
    public boolean isSatisfied(EntityAgeable npc) {
        return npc.getDistanceSq(pos) <= 1D;
    }
}

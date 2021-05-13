package uk.joshiejack.harvestcore.world.mine.dimension;

import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import org.apache.commons.lang3.tuple.Pair;

public class OverworldTeleporter implements ITeleporter {
    private final Town<?> town;

    public OverworldTeleporter(Town<?> town) {
        this.town = town;
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        Pair<BlockPos, Rotation> target = town.getLandRegistry().getWaypoint(Mine.BY_ID.get(entity.world.provider.getDimension()).getExitPoint());
        if (entity.timeUntilPortal == 0) {
            entity.timeUntilPortal = 10;
            entity.rotationYaw = target.getValue().rotate(EnumFacing.EAST).getHorizontalAngle();
            if (entity instanceof EntityPlayerMP) {
                ReflectionHelper.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP) entity, true, "invulnerableDimensionChange", "field_184851_cj");
            }

            entity.setPositionAndUpdate(target.getKey().getX() + 0.5D, target.getKey().getY(), target.getKey().getZ() + 0.5D);
        }
    }
}

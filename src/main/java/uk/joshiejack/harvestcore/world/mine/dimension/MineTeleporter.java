package uk.joshiejack.harvestcore.world.mine.dimension;

import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.mine.MineHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class MineTeleporter implements ITeleporter {
    private final Mine mine;
    private final int id; //id we're targeting

    public MineTeleporter(Mine mine, int id) {
        this.id = id;
        this.mine = mine;
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        MineHelper.teleportToFloor(entity, mine, id, 1, true);
    }
}

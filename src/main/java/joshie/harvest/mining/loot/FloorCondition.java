package joshie.harvest.mining.loot;


import joshie.harvest.mining.MiningHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class FloorCondition implements LootCondition {
    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            return testFloor(MiningHelper.getFloor(player.world.getChunkFromBlockCoords(new BlockPos(player)).x, (int) player.posY));
        }

        return false;
    }

    public abstract boolean testFloor(int floor);
}
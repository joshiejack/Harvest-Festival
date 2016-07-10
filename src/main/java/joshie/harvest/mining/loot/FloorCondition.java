package joshie.harvest.mining.loot;


import joshie.harvest.mining.MiningTicker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Random;

public abstract class FloorCondition implements LootCondition {
    @Override
    public boolean testCondition(Random rand, LootContext context) {
        EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
        if (player != null) {
            return testFloor(MiningTicker.getFloor(player.worldObj.getChunkFromBlockCoords(new BlockPos(player)).xPosition, (int) player.posY));
        }

        return false;
    }

    public abstract boolean testFloor(int floor);
}


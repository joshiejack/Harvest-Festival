package uk.joshiejack.economy.api.gold;

import net.minecraft.world.World;

public interface IVault {
    void increaseGold(World world, long gold);
}

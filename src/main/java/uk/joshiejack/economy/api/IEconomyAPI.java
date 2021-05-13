package uk.joshiejack.economy.api;

import uk.joshiejack.economy.api.gold.IVault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IEconomyAPI {
    IVault getVaultForPlayer(World world, EntityPlayer player);
}

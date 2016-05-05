package joshie.harvest.api.buildings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISpecialPurchaseRules {
    boolean canBuy(World world, EntityPlayer player);
}

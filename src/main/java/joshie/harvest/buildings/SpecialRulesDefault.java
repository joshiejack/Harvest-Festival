package joshie.harvest.buildings;

import joshie.harvest.api.shops.ISpecialPurchaseRules;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpecialRulesDefault implements ISpecialPurchaseRules {
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return true;
    }
}

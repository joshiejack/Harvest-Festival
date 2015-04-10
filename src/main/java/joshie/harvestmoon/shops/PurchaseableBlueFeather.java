package joshie.harvestmoon.shops;

import java.util.List;

import joshie.harvestmoon.api.shops.IPurchaseable;
import joshie.harvestmoon.core.util.Translate;
import joshie.harvestmoon.core.util.generic.Text;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableBlueFeather implements IPurchaseable {
    private ItemStack[] stacks;
    private int cost;
    
    public PurchaseableBlueFeather(int cost, ItemStack... stacks) {
        this.cost = cost;
        this.stacks = stacks;
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return true;
        //return PlayerHelper.isElligibleToMarry(player);
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public ItemStack[] getProducts() {
        return stacks;
    }

    @Override
    public void addTooltip(List list) {
        list.add(Text.WHITE + stacks[0].getDisplayName());
        list.add(Translate.translate("marriage"));
    }
}

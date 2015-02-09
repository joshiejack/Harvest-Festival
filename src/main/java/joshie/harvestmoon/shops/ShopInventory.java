package joshie.harvestmoon.shops;

import java.util.HashSet;

import net.minecraft.world.World;

public class ShopInventory {
    private HashSet<IPurchaseable> contents = new HashSet();

    /** Whether or not the shop is currently open at this time or season **/
    public boolean isOpen(World world) {
        return true;
    }

    public void add(IPurchaseable item) {
        this.contents.add(item);
    }
}

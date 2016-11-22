package joshie.harvest.shops.data;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.shops.Shop;
import joshie.harvest.shops.packet.PacketSyncSold;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class ShopData {
    private HashMap<Shop, ShopInventory> data = new HashMap<>();

    public void onPurchasableHandled(EntityPlayer player, Shop shop, IPurchasable purchasable) {
        data.putIfAbsent(shop, new ShopInventory(shop));
        if (purchasable.getCost() < 0) data.get(shop).onItemSoldToShop(purchasable);
        else data.get(shop).onItemPurchasedFromShop(purchasable);
        purchasable.onPurchased(player);
    }

    public boolean canList(Shop shop, IPurchasable purchasable) {
        data.putIfAbsent(shop, new ShopInventory(shop));
        return data.get(shop).canList(purchasable);
    }

    public long getSellValue(Shop shop, IPurchasable purchasable) {
        if (purchasable.getCost() >= 0) return purchasable.getCost();
        else {
            data.putIfAbsent(shop, new ShopInventory(shop));
            return data.get(shop).getAdjustedSellToShopValue(purchasable);
        }
    }

    public void newDay(World world, UUID uuid) {
        data.values().stream().forEach(ShopInventory::newDay);
        //Update all clients on the new values for this shop
        PacketHandler.sendToDimension(world.provider.getDimension(), new PacketSyncSold(uuid, writeToNBT(new NBTTagCompound())));
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTHelper.readMap("Shops", ShopInventory.class, data, nbt);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTHelper.writeMap("Shops", nbt, data);
        return nbt;
    }
}

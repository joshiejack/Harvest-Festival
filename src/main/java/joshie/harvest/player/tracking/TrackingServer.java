package joshie.harvest.player.tracking;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.generic.CollectionHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.holders.CropSoldStack;
import joshie.harvest.core.util.holders.ItemStackHolder;
import joshie.harvest.core.util.holders.SellHolderStack;
import joshie.harvest.crops.Crop;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.packets.PacketSyncObtained;
import joshie.harvest.player.packets.PacketSyncObtainedSet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.Iterator;

public class TrackingServer extends Tracking {
    private HashSet<SellHolderStack> toBeShipped = new HashSet<>(); //What needs to be sold

    public PlayerTrackerServer master;
    public TrackingServer(PlayerTrackerServer master) {
        this.master = master;
    }

    public void sync(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncObtainedSet(obtained), player);
    }

    @Override
    public void addAsObtained(ItemStack stack) {
        obtained.add(ItemStackHolder.of(stack));
        PacketHandler.sendToClient(new PacketSyncObtained(stack), master.getAndCreatePlayer());
    }

    public void onHarvested(Crop crop) {
        CollectionHelper.mergeCollection(CropSoldStack.of(crop), cropTracker);
    }

    public boolean addForShipping(ItemStack item) {
        long sell = HFApi.shipping.getSellValue(item);
        SellHolderStack stack = SellHolderStack.of(item, sell);
        CollectionHelper.mergeCollection(stack, toBeShipped);
        return sell >= 0;
    }

    public long newDay() {
        long sold = 0; //Loop through the to ship, get the money and remove them
        Iterator<SellHolderStack> forSale = toBeShipped.iterator();
        while (forSale.hasNext()) {
            SellHolderStack stack = forSale.next();
            sold += stack.getSellValue();
            CollectionHelper.mergeCollection(stack, sellTracker);
            forSale.remove();
        }

        return sold;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        cropTracker = NBTHelper.readHashSet(CropSoldStack.class, nbt.getTagList("CropsHarvested", 10));
        sellTracker = NBTHelper.readHashSet(SellHolderStack.class, nbt.getTagList("ItemsSold", 10));
        obtained = NBTHelper.readHashSet(ItemStackHolder.class, nbt.getTagList("ItemsObtained", 10));
        toBeShipped = NBTHelper.readHashSet(SellHolderStack.class, nbt.getTagList("ToBeShipped", 10));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("CropsHarvested", NBTHelper.writeCollection(cropTracker));
        nbt.setTag("ItemsSold", NBTHelper.writeCollection(sellTracker));
        nbt.setTag("ItemsObtained", NBTHelper.writeCollection(obtained));
        nbt.setTag("ToBeShipped", NBTHelper.writeCollection(toBeShipped));
        return nbt;
    }
}
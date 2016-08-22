package joshie.harvest.player.tracking;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.generic.CollectionHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.holders.ItemStackHolder;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.packets.PacketSyncObtained;
import joshie.harvest.player.packets.PacketSyncObtainedSet;
import joshie.harvest.player.packets.PacketSyncRecipes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.Iterator;

public class TrackingServer extends Tracking {
    //TODO: Add stat tracking, displayable on client  || private Set<CropHarvested> cropTracker = new HashSet<>(); //Crops that have been harvested
    //TODO: Add stat tracking, displayable on client  || private Set<StackSold> sellTracker = new HashSet<>(); //Items That have been sold
    private HashSet<StackSold> toBeShipped = new HashSet<>(); //What needs to be sold

    public PlayerTrackerServer master;
    public TrackingServer(PlayerTrackerServer master) {
        this.master = master;
    }

    public void sync(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncObtainedSet(obtained), player);
        PacketHandler.sendToClient(new PacketSyncRecipes(recipes), player);
    }

    @Override
    public void addAsObtained(ItemStack stack) {
        obtained.add(ItemStackHolder.of(stack));
        PacketHandler.sendToClient(new PacketSyncObtained(stack), master.getAndCreatePlayer());
    }

    /*TODO: Add stat tracking, displayable on client ||
    public void onHarvested(Crop crop) {
        CollectionHelper.mergeCollection(CropHarvested.of(crop), cropTracker);
    }*/

    public boolean addForShipping(ItemStack item) {
        long sell = HFApi.shipping.getSellValue(item);
        StackSold stack = StackSold.of(item, sell);
        CollectionHelper.mergeCollection(stack, toBeShipped);
        return sell >= 0;
    }

    public long newDay() {
        long sold = 0; //Loop through the to ship, get the money and remove them
        Iterator<StackSold> forSale = toBeShipped.iterator();
        while (forSale.hasNext()) {
            StackSold stack = forSale.next();
            sold += stack.getSellValue();
            //TODO: Add stat tracking, displayable on client || CollectionHelper.mergeCollection(stack, sellTracker);
            forSale.remove();
        }

        return sold;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        //TODO: Add stat tracking, displayable on client || cropTracker = NBTHelper.readHashSet(CropHarvested.class, nbt.getTagList("CropsHarvested", 10));
        //TODO: Add stat tracking, displayable on client || sellTracker = NBTHelper.readHashSet(StackSold.class, nbt.getTagList("ItemsSold", 10));
        obtained = NBTHelper.readHashSet(ItemStackHolder.class, nbt.getTagList("ItemsObtained", 10));
        toBeShipped = NBTHelper.readHashSet(StackSold.class, nbt.getTagList("ToBeShipped", 10));
        recipes = NBTHelper.readResourceSet(nbt.getTagList("Recipes", 8));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        //TODO: Add stat tracking, displayable on client  || nbt.setTag("CropsHarvested", NBTHelper.writeCollection(cropTracker));
        //TODO: Add stat tracking, displayable on client  || nbt.setTag("ItemsSold", NBTHelper.writeCollection(sellTracker));
        nbt.setTag("ItemsObtained", NBTHelper.writeCollection(obtained));
        nbt.setTag("ToBeShipped", NBTHelper.writeCollection(toBeShipped));
        nbt.setTag("Recipes", NBTHelper.writeResourceSet(recipes));
        return nbt;
    }
}
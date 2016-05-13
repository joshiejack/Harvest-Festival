package joshie.harvest.player.tracking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.generic.CollectionHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.Iterator;

public class TrackingDataServer extends TrackingData {
    private HashSet<SellHolderStack> toBeShipped = new HashSet<>(); //What needs to be sold

    //TODO: Sync Stats for reading in a gui
    public void sync(EntityPlayerMP player) {
        // TODO Auto-generated method stub
    }

    public void onHarvested(ICrop crop) {
        CollectionHelper.mergeCollection(new CropHolderStack(crop), cropTracker);
        HFTrackers.markDirty();
    }

    public boolean addForShipping(ItemStack item) {
        long sell = HFApi.shipping.getSellValue(item);
        SellHolderStack stack = new SellHolderStack(item, sell);
        CollectionHelper.mergeCollection(stack, toBeShipped);
        HFTrackers.markDirty();
        return sell >= 0;
    }

    public long newDay() {
        long sold = 0; //Loop through the to ship, get the money and remove them
        Iterator<SellHolderStack> forSale = toBeShipped.iterator();
        while (forSale.hasNext()) {
            SellHolderStack stack = forSale.next();
            sold += stack.sell;
            CollectionHelper.mergeCollection(stack, sellTracker);
            forSale.remove();
        }

        return sold;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        //Read in the CropsHarvested
        cropTracker = NBTHelper.readHashSet(CropHolderStack.class, nbt.getTagList("CropsHarvested", 10));
        //Read in the ItemsSold
        sellTracker = NBTHelper.readHashSet(SellHolderStack.class, nbt.getTagList("ItemsSold", 10));
        //Read in the Obtained List
        obtained = NBTHelper.readHashSet(ItemHolderStack.class, nbt.getTagList("ItemsObtained", 10));
        //Read in the ToBeShippedList
        toBeShipped = NBTHelper.readHashSet(SellHolderStack.class, nbt.getTagList("ToBeShipped", 10));
    }

    public void writeToNBT(NBTTagCompound nbt) {
        //Saving the CropsHarvested
        nbt.setTag("CropsHarvested", NBTHelper.writeCollection(cropTracker));
        //Saving the Sold List
        nbt.setTag("ItemsSold", NBTHelper.writeCollection(sellTracker));
        //Saving the Obtained List
        nbt.setTag("ItemsObtained", NBTHelper.writeCollection(obtained));
        //Saving the to be shipped list
        nbt.setTag("ToBeShipped", NBTHelper.writeCollection(toBeShipped));
    }
}
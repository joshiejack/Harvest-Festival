package joshie.harvest.player.tracking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
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
        CollectionHelper.mergeCollection(new CropSoldStack(crop), cropTracker);
    }

    public boolean addForShipping(ItemStack item) {
        long sell = HFApi.shipping.getSellValue(item);
        SellHolderStack stack = new SellHolderStack(item, sell);
        CollectionHelper.mergeCollection(stack, toBeShipped);
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
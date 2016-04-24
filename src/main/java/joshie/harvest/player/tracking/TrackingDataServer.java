package joshie.harvest.player.tracking;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.SafeStack;
import joshie.harvest.core.util.SellStack;
import joshie.harvest.crops.CropData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class TrackingDataServer extends TrackingData {
    private ArrayList<SellStack> toBeShipped = new ArrayList(); //What needs to be sold

    //TODO: Sync Stats for reading in a gui
    public void sync(EntityPlayerMP player) {
        // TODO Auto-generated method stub
        
    }
    
    public void onHarvested(ICropData data) {
        cropTracker.put(data, cropTracker.get(data) != null ? cropTracker.get(data) + 1 : 0);

        HFTrackers.markDirty();
    }

    public void addSold(SellStack stack) {
        if (sellTracker.contains(stack)) {
            for (SellStack safe : sellTracker) {
                if (safe.equals(stack)) {
                    safe.add(stack.sell);
                }
            }
        } else {
            sellTracker.add(stack);
        }
        
        HFTrackers.markDirty();
    }

    public boolean addForShipping(ItemStack stack) {
        long sell = 0;
        if (stack.getItem() instanceof IShippable) {
            sell = ((IShippable) stack.getItem()).getSellValue(stack);
        }

        SellStack check = new SellStack(stack, sell);
        if (toBeShipped.contains(check)) {
            for (SellStack safe : toBeShipped) {
                if (safe.equals(check)) {
                    safe.add(sell);
                }
            }
        } else {
            toBeShipped.add(check);
        }
        
        HFTrackers.markDirty();
        return sell >= 0;
    }

    public long newDay() {
        long sold = 0;
        Iterator<SellStack> forSale = toBeShipped.iterator();
        while (forSale.hasNext()) {
            SellStack stack = forSale.next();
            sold += stack.sell;
            addSold(stack);
            forSale.remove();
        }

        return sold;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        //Read in the CropsHarvested
        NBTTagList crops = nbt.getTagList("CropsHarvested", 10);
        for (int i = 0; i < crops.tagCount(); i++) {
            NBTTagCompound tag = crops.getCompoundTagAt(i);
            ICropData data = new CropData();
            data.readFromNBT(tag);
            int amount = nbt.getInteger("Amount");
            cropTracker.put(data, amount);
        }

        //Read in the ItemsSold
        NBTTagList sold = nbt.getTagList("ItemsSold", 10);
        for (int i = 0; i < sold.tagCount(); i++) {
            NBTTagCompound tag = sold.getCompoundTagAt(i);
            String name = tag.getString("ItemName");
            int damage = tag.getShort("ItemDamage");
            long sell = tag.getLong("SellValue");
            int amount = tag.getInteger("Amount");
            sellTracker.add(new SellStack(name, damage, amount, sell));
        }

        //Read in the Obtained List
        NBTTagList obtainedList = nbt.getTagList("ItemsObtained", 10);
        for (int i = 0; i < obtainedList.tagCount(); i++) {
            NBTTagCompound tag = obtainedList.getCompoundTagAt(i);
            String name = tag.getString("ItemName");
            int damage = tag.getShort("ItemDamage");
            obtained.add(new SafeStack(name, damage));
        }

        //Read in the ToBeShippedList
        NBTTagList shipped = nbt.getTagList("ToBeShipped", 10);
        for (int i = 0; i < shipped.tagCount(); i++) {
            NBTTagCompound tag = shipped.getCompoundTagAt(i);
            String name = tag.getString("ItemName");
            int damage = tag.getShort("ItemDamage");
            long sell = tag.getInteger("SellValue");
            int amount = tag.getInteger("Amount");
            toBeShipped.add(new SellStack(name, damage, amount, sell));
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        //Saving the CropsHarvested
        NBTTagList crops = new NBTTagList();
        for (Map.Entry<ICropData, Integer> entry : cropTracker.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            entry.getKey().writeToNBT(tag);
            tag.setInteger("Amount", entry.getValue());
            crops.appendTag(tag);
        }

        nbt.setTag("CropsHarvested", crops);

        /////////////////////////////////////////////////////////////////////

        //Saving the Sold List
        NBTTagList sold = new NBTTagList();
        for (SellStack stack : sellTracker) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ItemName", stack.item);
            tag.setShort("ItemDamage", (short) stack.damage);
            tag.setLong("SellValue", stack.sell);
            tag.setInteger("Amount", stack.amount);
            sold.appendTag(tag);
        }

        nbt.setTag("ItemsSold", sold);

        ////////////////////////////////////////////////////////////////////////////

        //Saving the Obtained List
        NBTTagList obtainedList = new NBTTagList();
        for (SafeStack stack : obtained) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ItemName", stack.item);
            tag.setShort("ItemDamage", (short) stack.damage);
            obtainedList.appendTag(tag);
        }

        nbt.setTag("ItemsObtained", obtainedList);

        ////////////////////////////////////////////////////////////////////////////

        //Saving the ToBeShippedList
        NBTTagList shipped = new NBTTagList();
        for (SellStack stack : toBeShipped) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ItemName", stack.item);
            tag.setShort("ItemDamage", (short) stack.damage);
            tag.setLong("SellValue", stack.sell);
            tag.setInteger("Amount", stack.amount);
            shipped.appendTag(tag);
        }

        nbt.setTag("ToBeShipped", shipped);
    }
}

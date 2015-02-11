package joshie.harvestmoon.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import joshie.harvestmoon.crops.CropData;
import joshie.harvestmoon.util.IData;
import joshie.harvestmoon.util.SellStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TrackingStats implements IData {
    private HashMap<CropData, Integer> cropTracker = new HashMap(); //How many of this crop has been Harvested
    private HashSet<SellStack> sellTracker = new HashSet(); //What has been sold so far
    
    public PlayerDataServer master;
    public TrackingStats(PlayerDataServer master) {
        this.master = master;
    }
    
    public void onHarvested(CropData data) {
        cropTracker.put(data, cropTracker.get(data) != null ? cropTracker.get(data) + 1 : 0);
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
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        //Read in the CropsHarvested
        NBTTagList crops = nbt.getTagList("CropsHarvested", 10);
        for (int i = 0; i < crops.tagCount(); i++) {
            NBTTagCompound tag = crops.getCompoundTagAt(i);
            CropData data = new CropData();
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
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        //Saving the CropsHarvested
        NBTTagList crops = new NBTTagList();
        for (Map.Entry<CropData, Integer> entry : cropTracker.entrySet()) {
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
    }
}

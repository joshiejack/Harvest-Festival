package harvestmoon.player;

import harvestmoon.HarvestMoon;
import harvestmoon.util.IData;
import harvestmoon.util.IShippable;
import harvestmoon.util.SellStack;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ShippingStats implements IData {
    private ArrayList<SellStack> toBeShipped = new ArrayList(); //What needs to be sold

    public boolean addForShipping(ItemStack stack) {
        int sell = 0 ;
        if(stack.getItem() instanceof IShippable) {
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
        
        return sell >= 0;
    }

    public int newDay(EntityPlayerMP player) {
        int sold = 0;
        Iterator<SellStack> forSale = toBeShipped.iterator();
        while (forSale.hasNext()) {
            SellStack stack = forSale.next();
            sold += stack.sell;
            HarvestMoon.handler.getServer().getPlayerData(player).addSold(stack);
            forSale.remove();
        }
        
        return sold;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        //Read in the ToBeShippedList
        NBTTagList shipped = nbt.getTagList("ToBeShipped", 10);
        for (int i = 0; i < shipped.tagCount(); i++) {
            NBTTagCompound tag = shipped.getCompoundTagAt(i);
            String name = tag.getString("ItemName");
            int damage = tag.getShort("ItemDamage");
            int sell = tag.getInteger("SellValue");
            int amount = tag.getInteger("Amount");
            toBeShipped.add(new SellStack(name, damage, sell, amount));
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        //Saving the ToBeShippedList
        NBTTagList shipped = new NBTTagList();
        for (SellStack stack : toBeShipped) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ItemName", stack.item);
            tag.setShort("ItemDamage", (short) stack.damage);
            tag.setInteger("SellValue", stack.sell);
            tag.setInteger("Amount", stack.amount);
            shipped.appendTag(tag);
        }

        nbt.setTag("ToBeShipped", shipped);
    }
}

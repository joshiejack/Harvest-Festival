package joshie.harvest.player.tracking;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.recipe.MealImpl;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.CollectionHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.holder.ItemStackHolder;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.packet.PacketSyncObtained;
import joshie.harvest.player.packet.PacketSyncObtainedSet;
import joshie.harvest.player.packet.PacketSyncRecipes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.Iterator;

public class TrackingServer extends Tracking {
    private HashSet<StackSold> toBeShipped = new HashSet<>(); //What needs to be sold
    public final PlayerTrackerServer master;

    public TrackingServer(PlayerTrackerServer master) {
        this.master = master;
    }

    @Override
    public boolean learnRecipe(MealImpl recipe) {
        if (super.learnRecipe(recipe)) {
            if (recipes.size() >= 50 && master.getAndCreatePlayer() != null) {
                master.getAndCreatePlayer().addStat(HFAchievements.recipes);
            }

            return true;
        } else return false;
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
            forSale.remove();
        }

        return sold;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        obtained = NBTHelper.readHashSet(ItemStackHolder.class, nbt.getTagList("ItemsObtained", 10));
        toBeShipped = NBTHelper.readHashSet(StackSold.class, nbt.getTagList("ToBeShipped", 10));
        recipes = NBTHelper.readResourceSet(nbt, "Recipes");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("ItemsObtained", NBTHelper.writeCollection(obtained));
        nbt.setTag("ToBeShipped", NBTHelper.writeCollection(toBeShipped));
        nbt.setTag("Recipes", NBTHelper.writeResourceSet(recipes));
        return nbt;
    }
}
package joshie.harvest.player.tracking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.helpers.HolderHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.holders.ItemStackHolder;
import joshie.harvest.knowledge.gui.stats.CollectionHelper;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.packet.*;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TrackingServer extends Tracking {
    private Set<StackSold> toBeShipped = new HashSet<>(); //What needs to be sold
    private Set<StackSold> shipped = new HashSet<>();
    private int giftsGiven;
    public final PlayerTrackerServer master;

    public TrackingServer(PlayerTrackerServer master) {
        this.master = master;
        addDefaultRecipes();
    }

    @Override
    public boolean learnRecipe(Recipe recipe) {
        if (super.learnRecipe(recipe)) {
            EntityPlayer player = master.getAndCreatePlayer();
            if (recipes.size() >= 50 && player != null) {
                player.addStat(HFAchievements.recipes);
                HFApi.quests.completeQuestConditionally(Quests.SELL_TREES, player);
            }

            return true;
        } else return false;
    }

    public Set<StackSold> getShipped() {
        return shipped;
    }

    public void sync(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncObtainedSet(obtained), player);
        PacketHandler.sendToClient(new PacketSyncRecipes(recipes), player);
        PacketHandler.sendToClient(new PacketSyncNotes(notes), player);
        PacketHandler.sendToClient(new PacketSyncUnread(unread), player);
    }

    public void addGift() {
        giftsGiven++;
        if (giftsGiven >= 300) {
            EntityPlayer player = master.getAndCreatePlayer();
            if (player != null) {
                HFApi.quests.completeQuestConditionally(Quests.SELL_SWEET_POTATO, player);
            }
        }
    }

    @Override
    public void addAsObtained(ItemStack stack) {
        obtained.add(ItemStackHolder.of(stack));
        PacketHandler.sendToClient(new PacketSyncObtained(stack), master.getAndCreatePlayer());
    }

    private void addDefaultRecipes() {
        //Learn all the default recipes
        for (Recipe recipe: Recipe.REGISTRY) {
            if (recipe.isDefault() && !recipes.contains(recipe.getRegistryName())) {
                recipes.add(recipe.getRegistryName());
            }
        }
    }

    public boolean addForShipping(ItemStack item) {
        long sell = HFApi.shipping.getSellValue(item);
        StackSold stack = StackSold.of(item, sell);
        HolderHelper.mergeCollection(stack, toBeShipped);
        return sell >= 0;
    }

    public long newDay() {
        long sold = 0; //Loop through the to ship, get the money and remove them
        Iterator<StackSold> forSale = toBeShipped.iterator();
        while (forSale.hasNext()) {
            StackSold stack = forSale.next();
            sold += stack.getSellValue();
            HolderHelper.mergeCollection(stack, shipped); //Mark this item as having been shipped
            if (CollectionHelper.isInShippingCollection(stack.getStack())) {
                addAsObtained(stack.getStack()); //Mark the item as having been collected
            }

            forSale.remove();
        }

        return sold;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        giftsGiven = nbt.getInteger("GiftsGiven");
        obtained = NBTHelper.readHashSet(ItemStackHolder.class, nbt.getTagList("ItemsObtained", 10));
        toBeShipped = NBTHelper.readHashSet(StackSold.class, nbt.getTagList("ToBeShipped", 10));
        recipes = NBTHelper.readResourceSet(nbt, "Recipes");
        shipped = NBTHelper.readHashSet(StackSold.class, nbt.getTagList("Shipped", 10));
        notes = NBTHelper.readResourceSet(nbt, "Notes");
        unread = NBTHelper.readResourceSet(nbt, "Unread");
        addDefaultRecipes();
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("GiftsGiven", giftsGiven);
        nbt.setTag("ItemsObtained", NBTHelper.writeCollection(obtained));
        nbt.setTag("ToBeShipped", NBTHelper.writeCollection(toBeShipped));
        nbt.setTag("Recipes", NBTHelper.writeResourceSet(recipes));
        nbt.setTag("Shipped", NBTHelper.writeCollection(shipped));
        nbt.setTag("Notes", NBTHelper.writeResourceSet(notes));
        nbt.setTag("Unread", NBTHelper.writeResourceSet(unread));
        return nbt;
    }
}
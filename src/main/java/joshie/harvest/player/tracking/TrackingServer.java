package joshie.harvest.player.tracking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.TargetType;
import joshie.harvest.core.helpers.HolderHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.holders.ItemStackHolder;
import joshie.harvest.knowledge.gui.stats.CollectionHelper;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.packet.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TrackingServer extends Tracking {
    private Set<StackSold> toBeShipped = new HashSet<>(); //What needs to be sold
    private Set<StackSold> shipped = new HashSet<>();
    private boolean hasWonCookingContest;
    private int giftsGiven;
    public final PlayerTrackerServer master;

    public TrackingServer(PlayerTrackerServer master) {
        this.master = master;
        addDefaultRecipes();
    }

    public void setHasWonCookingContest() {
        this.hasWonCookingContest = true;
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
    }

    @Override
    public void addAsObtained(ItemStack stack) {
        obtained.add(ItemStackHolder.of(stack));
        PacketHandler.sendToClient(new PacketSyncObtained(stack), master.getAndCreatePlayer());
    }

    public boolean addForShipping(ItemStack item) {
        long sell = HFApi.shipping.getSellValue(item);
        if (hasWonCookingContest && CollectionHelper.isInCookingCollection(item)) {
            sell *= 1.1;
        }

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

    private void addDefaultRecipes() {
        //Learn all the default recipes
        Recipe.REGISTRY.values().stream().filter(recipe -> recipe.isDefault() && !recipes.contains(recipe.getResource())).forEachOrdered(recipe -> recipes.add(recipe.getResource()));
    }

    private void addLearntNotes() {
        for (Quest quest: Quest.REGISTRY) {
            if (quest.getQuestType() == TargetType.PLAYER) {
                if (master.getQuests().getFinished().contains(quest)) {
                    quest.getNotes().forEach(this :: learnNote);
                }
            }
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        hasWonCookingContest = nbt.getBoolean("HasWon");
        giftsGiven = nbt.getInteger("GiftsGiven");
        obtained = NBTHelper.readHashSet(ItemStackHolder.class, nbt.getTagList("ItemsObtained", 10));
        toBeShipped = NBTHelper.readHashSet(StackSold.class, nbt.getTagList("ToBeShipped", 10));
        recipes = NBTHelper.readResourceSet(nbt, "Recipes");
        shipped = NBTHelper.readHashSet(StackSold.class, nbt.getTagList("Shipped", 10));
        notes = NBTHelper.readResourceSet(nbt, "Notes");
        unread = NBTHelper.readResourceSet(nbt, "Unread");
        addDefaultRecipes();
        addLearntNotes();
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("HasWon", hasWonCookingContest);
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
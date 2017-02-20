package joshie.harvest.player.tracking;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.player.IPlayerTracking;
import joshie.harvest.core.util.holders.ItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class Tracking implements IPlayerTracking {
    protected Set<ItemStackHolder> obtained = new HashSet<>(); //Items that have been obtained
    protected Set<ResourceLocation> recipes = new HashSet<>(); //Recipe Learnt
    protected Set<ResourceLocation> notes = new HashSet<>(); //Notes Learnt
    protected Set<ResourceLocation> unread = new HashSet<>(); //Things we haven't read yet

    public Set<ResourceLocation> getReadStatus() {
        return unread;
    }

    public boolean learnRecipe(Recipe recipe) {
        if (recipe == null) {
            recipes.clear();
            return false;
        } else return recipes.add(recipe.getRegistryName());
    }

    @Override
    public boolean learnNote(Note note) {
        if (note == null) {
            notes.clear();
            return false;
        } else return notes.add(note.getResource());
    }

    @Override
    public void addAsObtained(ItemStack stack) {
        obtained.add(ItemStackHolder.of(stack));
    }

    public boolean hasObtainedItem(ItemStack stack) {
        for (ItemStackHolder holder: obtained) {
            if (holder.matches(stack)) return true;
        }

        return false;
    }
}
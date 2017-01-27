package joshie.harvest.api.calendar;

import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.quests.Quest;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Festival {
    public static final Festival NONE = new Festival(new ResourceLocation("harvestfestival", "none"));
    private final ResourceLocation resource;
    private boolean shopsOpen;
    private boolean hidden;
    private Quest quest;
    private Note note;

    public Festival(@Nonnull ResourceLocation resource) {
        this.resource = resource;
    }

    /** The note that gets added when this festival is
     *  around for the first time */
    public Festival setNote(Note note) {
        this.note = note;
        return this;
    }

    /** The quest that gets activated by this festival **/
    public Festival setQuest(Quest quest) {
        this.quest = quest;
        return this;
    }

    /** Call this to make shops open on this festival **/
    public Festival setShopsOpen() {
        this.shopsOpen = true;
        return this;
    }

    /** Hide this festival from the calendar **/
    public Festival setHidden() {
        this.hidden = true;
        return this;
    }

    @Nullable
    public Note getNote() {
        return note;
    }

    @Nonnull
    public ResourceLocation getResource() {
        return resource;
    }

    @Nullable
    public Quest getQuest() {
        return quest;
    }

    public boolean doShopsOpen() {
        return shopsOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Festival festival = (Festival) o;
        return resource.equals(festival.resource);
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }
}

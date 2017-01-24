package joshie.harvest.api.calendar;

import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.quests.Quest;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Festival {
    private final ResourceLocation resource;
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

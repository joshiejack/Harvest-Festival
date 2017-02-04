package joshie.harvest.api.calendar;

import joshie.harvest.api.core.Letter;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.quests.Quest;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

import static joshie.harvest.api.calendar.CalendarDate.DAYS_PER_SEASON;

public final class Festival {
    public static final HashMap<ResourceLocation, Festival> REGISTRY = new HashMap<>();
    public static final Festival NONE = new Festival(new ResourceLocation("harvestfestival", "none"));
    private final ResourceLocation resource;
    private boolean affectsGround;
    private boolean shopsOpen;
    private boolean hidden;
    private int length;
    private Quest quest;
    private Letter letter;
    private Note note;

    public Festival(@Nonnull ResourceLocation resource) {
        this.resource = resource;
        this.length = 3;
        this.affectsGround = true;
        REGISTRY.put(resource, this);
    }

    /** The note that gets added when this festival is
     *  around for the first time */
    public Festival setNote(Note note) {
        this.note = note;
        return this;
    }

    /** The letter that gets sent to the town mailbox **/
    public Festival setLetter(Letter letter) {
        this.letter = letter;
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

    /** Set the festival length
     *  @param length   the number of days the festival will stay up **/
    public Festival setLength(int length) {
        this.length = length;
        return this;
    }

    /** Call this to make this festival change the look of the festival grounds **/
    public Festival setNoBuilding() {
        this.affectsGround = false;
        return this;
    }

    /** Returns how many days this festival lasts **/
    public int getFestivalLength() {
        return (int)(((double)length / 30D) * DAYS_PER_SEASON);
    }

    @Nullable
    public Note getNote() {
        return note;
    }

    @Nullable
    public Letter getLetter() {
        return letter;
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

    public boolean affectsFestivalGrounds() {
        return affectsGround;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Festival)) return false;
        Festival festival = (Festival) o;
        return resource.equals(festival.resource);
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }
}

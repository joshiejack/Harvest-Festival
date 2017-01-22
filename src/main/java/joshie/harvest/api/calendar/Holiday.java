package joshie.harvest.api.calendar;

import joshie.harvest.api.quests.Quest;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Holiday {
    private final ResourceLocation resource;
    private final Quest quest;

    public Holiday(@Nonnull ResourceLocation resource, @Nullable Quest quest) {
        this.resource = resource;
        this.quest = quest;
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
        Holiday holiday = (Holiday) o;
        return resource.equals(holiday.resource);
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }
}

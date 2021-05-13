package uk.joshiejack.penguinlib.events;

import net.minecraft.advancements.Advancement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Map;

public class RemoveAdvancementEvent extends WorldEvent {
    private final Map<ResourceLocation, Advancement> advancementMap;

    public RemoveAdvancementEvent(WorldServer world, Map<ResourceLocation, Advancement> advancements) {
        super(world);
        this.advancementMap = advancements;
    }

    public Map<ResourceLocation, Advancement> getAdvancementMap() {
        return advancementMap;
    }
}

package joshie.harvest.npc;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.npc.gift.GiftRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

@HFApiImplementation
public class NPCRegistry implements INPCRegistry {
    public static final IForgeRegistry<NPC> REGISTRY = new RegistryBuilder<NPC>().setName(new ResourceLocation("harvestfestival", "npcs")).setType(NPC.class).setIDRange(0, 32000).create();
    public static final NPCRegistry INSTANCE = new NPCRegistry();
    private final IGiftRegistry gifts = new GiftRegistry();

    private NPCRegistry() {}

    @Override
    public INPC register(ResourceLocation resource, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor) {
        return new NPC(resource, gender, age, new CalendarDate(dayOfBirth, seasonOfBirth, 1), insideColor, outsideColor);
    }

    @Override
    public IGiftRegistry getGifts() {
        return gifts;
    }
}
package joshie.harvest.npc;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.npc.NPC.Age;
import joshie.harvest.npc.NPC.Gender;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class NPCRegistry implements INPCRegistry {
    public static final FMLControlledNamespacedRegistry<NPC> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(MODID, "npcs"), NPC.class, null, 0, 32000, true, null, null, null);

    @Override
    public INPC register(ResourceLocation resource, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor) {
        return new NPC(resource, gender, age, HFApi.calendar.newDate(dayOfBirth, seasonOfBirth, 1), insideColor, outsideColor);
    }
}
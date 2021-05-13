package uk.joshiejack.settlements.proxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.util.EntityIDs;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.template.entities.PlaceableLiving;

public class CommonProxy {
    public void preInit() {
        ResourceLocation npcID = new ResourceLocation(Settlements.MODID,"npc");
        PlaceableLiving.CUSTOM_SPAWN_HANDLERS.put(npcID, (world, pos, rotation, nbt) -> {
            NPC npc = NPC.getNPCFromRegistry(new ResourceLocation(nbt.getString("NPC")));
            if (npc != NPC.NULL_NPC) {
                TownServer town = TownFinder.find(world, pos);
                town.getCensus().invite(npc.getRegistryName());
                AdventureDataLoader.get(world).markDirty();
            }

            return null;
        });

        EntityRegistry.registerModEntity(npcID, EntityNPC.class, "npc", EntityIDs.NPC, Settlements.instance, 80, 3, true);
    }

    public void postInit(){}
}

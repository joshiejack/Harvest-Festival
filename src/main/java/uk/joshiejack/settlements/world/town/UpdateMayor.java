package uk.joshiejack.settlements.world.town;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.network.town.people.PacketSyncMayor;
import uk.joshiejack.penguinlib.events.TeamChangedOwnerEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class UpdateMayor {
    @SubscribeEvent
    public static void onTeamChangedOwner(TeamChangedOwnerEvent event) {
        for (WorldServer world: DimensionManager.getWorlds()) {
            AdventureDataLoader.get(world).getTowns(world).stream().filter(town ->
                    town.getCharter().getTeamID().equals(event.getTeamUUID())).findFirst().ifPresent(t -> {
                        t.getCharter().setMayor(event.getNewOwner());
                        PenguinNetwork.sendToEveryone(new PacketSyncMayor(world.provider.getDimension(), t.getID(), t.getCharter().getMayor()));
            });
        }
    }
}

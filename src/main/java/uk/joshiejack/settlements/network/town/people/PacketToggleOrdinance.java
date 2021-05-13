package uk.joshiejack.settlements.network.town.people;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.client.WorldMap;
import uk.joshiejack.settlements.network.town.land.PacketCreateTown;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.people.Ordinance;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;

@PenguinLoader
public class PacketToggleOrdinance extends PacketCreateTown {
    private int dimension, id;
    private Ordinance ordinance;
    private boolean enact;

    public PacketToggleOrdinance(){}
    public PacketToggleOrdinance(Ordinance ordinance, int dimension, int town) {
        this.ordinance = ordinance;
        this.dimension = dimension;
        this.id = town;
    }

    public PacketToggleOrdinance(Ordinance ordinance, int dimension, int town, boolean enact) {
        this.ordinance = ordinance;
        this.enact = enact;
        this.dimension = dimension;
        this.id = town;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(ordinance.ordinal());
        buf.writeBoolean(enact);
        buf.writeInt(dimension);
        buf.writeInt(id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ordinance = Ordinance.values()[buf.readByte()];
        enact = buf.readBoolean();
        dimension = buf.readInt();
        id = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (!player.world.isRemote) {
            Town<?> town = AdventureDataLoader.get(player.world).getTownByID(dimension, id);
            PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
            if (town.getCharter().getTeamID().equals(team.getID())) {
                boolean set = !town.getGovernment().hasLaw(ordinance);
                town.getGovernment().setLaw(ordinance, set); //Set the law
                AdventureDataLoader.get(player.world).markDirty();
                //Sync back to the client
                PenguinNetwork.sendToEveryone(new PacketToggleOrdinance(ordinance, dimension, id, set));
            }
        } else WorldMap.getTownByID(dimension, id).getGovernment().setLaw(ordinance, enact);
    }
}

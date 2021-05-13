package uk.joshiejack.settlements.network.town.people;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketInviteNPC extends PenguinPacket {
    private ResourceLocation npc;

    public PacketInviteNPC() {}
    public PacketInviteNPC(ResourceLocation npc) {
        this.npc = npc;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, npc.toString());
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        npc = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TownServer town = TownFinder.find(player.world, player.getPosition()); //Don't create any towns here
        if (PenguinTeams.get(player.world).getTeamMembers(town.getCharter().getTeamID()).contains(PlayerHelper.getUUIDForPlayer(player))) {
            if (town.getCensus().isInvitable(npc)) {
                town.getCensus().invite(npc); //Now we need to resend the list
                town.getCensus().onNPCsChanged(player.world); //Invites changed
                AdventureDataLoader.get(player.world).markDirty(); //Save the invites
            }
        }
    }
}

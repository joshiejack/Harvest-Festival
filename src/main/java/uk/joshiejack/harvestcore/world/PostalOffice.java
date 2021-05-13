package uk.joshiejack.harvestcore.world;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.harvestcore.world.storage.Mailroom;
import uk.joshiejack.harvestcore.world.storage.SavedData;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.events.TeamChangedEvent;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.UUID;


@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class PostalOffice {
    @SubscribeEvent
    public static void onTeamChanged(TeamChangedEvent event) {
        Mailroom mymailroom = SavedData.getMailroom(event.getWorld(), event.getPlayer());
        for (UUID uuid: PenguinTeams.get(event.getWorld()).getTeamMembers(event.getNewTeam())) {
            if (!uuid.equals(event.getPlayer())) {
                Mailroom theirmailroom = SavedData.getMailroom(event.getWorld(), uuid);
                mymailroom.copyLetters(event.getWorld(), theirmailroom);
                break;
            }
        }
    }

    public static void deliver(World world, EntityPlayer player, Letter letter) {
        if (!world.isRemote) {
            SavedData.getMailroom(world, PlayerHelper.getUUIDForPlayer(player)).removeLetter(world, letter);
        }
    }

    public static void send(World world, EntityPlayer player, @Nonnull Letter letter) {
        if (!world.isRemote) {
            switch (letter.getGroup()) {
                case PLAYER:
                    SavedData.getMailroom(world, PlayerHelper.getUUIDForPlayer(player)).addLetter(world, letter);
                    break;
                case TEAM:
                    UUID team = PenguinTeams.get(world).getTeamUUIDForPlayer(player);
                    for (UUID uuid: PenguinTeams.get(world).getTeamMembers(team)) {
                        SavedData.getMailroom(world, uuid).addLetter(world, letter);
                    }

                    break;
                case GLOBAL:
                    SavedData.getMailrooms(world).forEach((d) -> d.addLetter(world, letter));
                    break;
            }
        }
    }
}

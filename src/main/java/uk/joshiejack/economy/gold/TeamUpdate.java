package uk.joshiejack.economy.gold;

import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.shipping.Market;
import uk.joshiejack.penguinlib.events.TeamChangedEvent;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Economy.MODID)
public class TeamUpdate {
    @SubscribeEvent
    public static void onTeamChanged(TeamChangedEvent event) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(event.getWorld(), event.getPlayer());
        if (player != null) {
            Bank.get(event.getWorld()).syncToPlayer(player);
            Market.get(event.getWorld()).getShippingForPlayer(player).syncToPlayer(player);
        }
    }
}

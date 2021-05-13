package uk.joshiejack.energy.events;

import uk.joshiejack.energy.Energy;
import uk.joshiejack.energy.EnergyConfig;
import uk.joshiejack.energy.EnergyFoodStats;
import uk.joshiejack.energy.packet.PacketSyncStats;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber(modid = Energy.MODID)
public class FoodStatsEvents {
    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onWorldCreated(WorldEvent.Load event) {
        if (EnergyConfig.forceNaturalRegenDisabling) event.getWorld().getGameRules().setOrCreateGameRule("naturalRegeneration", "false");
    }

    @SubscribeEvent
    public static void respawnEvent(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        if (!event.player.world.isRemote) replaceFoodStats(event.player);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void replaceFoodStats(EntityJoinWorldEvent event) {
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayer) {
            replaceFoodStats((EntityPlayer) event.getEntity());
        }
    }

    protected static void replaceFoodStats(EntityPlayer player) {
        if (!(player.getFoodStats() instanceof EnergyFoodStats)) {
            EnergyFoodStats stats = new EnergyFoodStats(player);
            stats.readNBT(player.getEntityData()); //Read in the instance we saved
            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(stats.maxHearts);
            ReflectionHelper.setPrivateValue(EntityPlayer.class, player, stats, "foodStats", "field_71100_bB");
        }

        PenguinNetwork.sendToClient(new PacketSyncStats((EnergyFoodStats)player.getFoodStats()), player);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCloning(PlayerEvent.Clone event) {
        ReflectionHelper.setPrivateValue(EntityPlayer.class, event.getEntityPlayer(), event.getOriginal().getFoodStats(), "foodStats", "field_71100_bB");
        if (event.getEntityPlayer().getFoodStats() instanceof EnergyFoodStats) {
            ((EnergyFoodStats)event.getEntityPlayer().getFoodStats()).setPlayer(event.getEntityPlayer());
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void saveFoodStats(PlayerLoggedOutEvent event) {
        if (event.player.getFoodStats() instanceof EnergyFoodStats) {
            event.player.getFoodStats().writeNBT(event.player.getEntityData());
        }
    }

    @SubscribeEvent
    public static void onPlayerTakenDamage(LivingAttackEvent event) {
        if (event.getSource() == DamageSource.STARVE) event.setCanceled(true);
    }
}

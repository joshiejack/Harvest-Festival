package uk.joshiejack.settlements.world.town.people;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.land.Interaction;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class Protection {
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntityLiving() instanceof EntityPlayer) {
            Town<?> town = TownFinder.find(event.getEntityLiving().world, event.getEntityLiving().getPosition());
            if (town.getGovernment().hasLaw(Ordinance.NO_KILL)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        Town<?> town = TownFinder.find(event.getEntityLiving().world, event.getEntityLiving().getPosition());
        if (town.getGovernment().hasLaw(Ordinance.BAN_ITEM_USAGE) && !(event.getItemStack().getItem() instanceof ItemFood)) {
            event.setCanceled(true);
        }
    }

    private static boolean isProtected(@Nullable EntityPlayer player, World world, BlockPos pos, Interaction interaction) {
        Town<?> town = TownFinder.find(world, pos);
        if (town.getGovernment().hasLaw(Ordinance.EXTERNAL_PROTECTION) && (player == null || !PenguinTeams.getTeamForPlayer(player).getID().equals(town.getCharter().getTeamID()))) {
            return true;
        }

        return town.getGovernment().hasLaw(Ordinance.INTERNAL_PROTECTION) && town.getLandRegistry().getFootprints(world, interaction).contains(pos);
    }
}

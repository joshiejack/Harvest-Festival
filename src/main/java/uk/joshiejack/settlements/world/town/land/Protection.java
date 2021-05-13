package uk.joshiejack.settlements.world.town.land;

import com.google.common.collect.Sets;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.people.Ordinance;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class Protection {
    static final Set<Block> RIGHT_CLICK_PREVENTION_LIST = Sets.newHashSet(Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, Blocks.TRAPDOOR);

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (isProtected(event.getEntityPlayer(), event.getEntityPlayer().world, event.getPos(), Interaction.BREAK, Ordinance.EXTERNAL_PROTECTION)) {
            event.setNewSpeed(0F);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDetonate(ExplosionEvent.Detonate event) {
        if (event.getAffectedBlocks().size() > 0) {
            BlockPos origin = new BlockPos(event.getExplosion().getPosition());
            Town<?> town = TownFinder.find(event.getWorld(), origin);
            if (town.getGovernment().hasLaw(Ordinance.INTERNAL_PROTECTION)) {
                List<BlockPos> footprints = town.getLandRegistry().getFootprints(event.getWorld(), Interaction.FOOTPRINT);
                event.getAffectedBlocks().removeAll(footprints);
                event.getAffectedEntities().removeIf(e -> footprints.contains(e.getPosition()));
            }

            if (town.getGovernment().hasLaw(Ordinance.EXTERNAL_PROTECTION)) {
                event.getAffectedBlocks().removeIf(pos -> TownFinder.find(event.getWorld(), pos) == town);
                event.getAffectedEntities().removeIf(entity -> TownFinder.find(event.getWorld(), entity.getPosition()) == town);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHarvestBlock(BlockEvent.HarvestDropsEvent event) {
        if (isProtected(event.getHarvester(), event.getWorld(), event.getPos(), Interaction.BREAK, Ordinance.EXTERNAL_PROTECTION)) {
            event.getDrops().clear();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (isProtected(event.getPlayer(), event.getWorld(), event.getPos(), Interaction.BREAK, Ordinance.EXTERNAL_PROTECTION)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityItemFrame) {
            if (isProtected(event.getEntityPlayer(), event.getWorld(), event.getPos(), Interaction.ENTITY_INTERACT, Ordinance.BAN_COMMUNICATION)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onActivate(PlayerInteractEvent.RightClickBlock event) {
        if (isProtected(event.getEntityPlayer(), event.getWorld(), event.getPos(), Interaction.RIGHT_CLICK, Ordinance.BAN_INTERACTION)) {
            event.setUseBlock(Event.Result.DENY);
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlaced(BlockEvent.PlaceEvent event) {
        if (isProtected(event.getPlayer(), event.getWorld(), event.getPos(), Interaction.RIGHT_CLICK, Ordinance.BAN_CONSTRUCTION)) {
            event.setCanceled(true);
        }
    }

    private static boolean isProtected(@Nullable EntityPlayer player, World world, BlockPos pos, Interaction interaction, Ordinance law) {
        Town<?> town = TownFinder.find(world, pos);
        if (town.getGovernment().hasLaw(law) && (player == null || !PenguinTeams.getTeamForPlayer(player).getID().equals(town.getCharter().getTeamID()))) {
            return true;
        }

        return town.getGovernment().hasLaw(Ordinance.INTERNAL_PROTECTION) && town.getLandRegistry().getFootprints(world, interaction).contains(pos);
    }
}

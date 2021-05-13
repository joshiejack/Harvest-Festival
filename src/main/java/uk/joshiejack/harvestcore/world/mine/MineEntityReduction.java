package uk.joshiejack.harvestcore.world.mine;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator.CHUNKS_PER_SECTION;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class MineEntityReduction {
    @SubscribeEvent
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (!event.isSpawner() && Mine.BY_ID.containsKey(event.getWorld().provider.getDimension()) && event.getEntityLiving() instanceof EntityLiving) {
            BlockPos target = new BlockPos(event.getX(), event.getY(), event.getZ());
            Chunk chunk = event.getWorld().getChunk(target);
            int tierNumber = chunk.z / CHUNKS_PER_SECTION; //The Tier of this section of the mine
            Tier tier = Mine.BY_ID.get(event.getWorld().provider.getDimension()).getTierFromInt(tierNumber);
            int floor = Math.max(40 - (int) Math.floor(event.getY() / 6), 1);
            int onFloor = EntityHelper.getEntities(EntityLiving.class, event.getWorld(), target, 48D, 4D).size();
            if (onFloor < 8 && tier.canSpawn((EntityLiving) event.getEntity(), floor) && event.getY() < 240) {
                event.setResult(Event.Result.ALLOW);
            } else {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}

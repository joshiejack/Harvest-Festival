package uk.joshiejack.harvestcore.client.renderer.fertilizer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.network.fertilizer.PacketRemoveFertilizer;
import uk.joshiejack.harvestcore.network.fertilizer.PacketRequestFertilizerChunkData;
import uk.joshiejack.harvestcore.registry.Fertilizer;
import uk.joshiejack.penguinlib.network.PenguinNetwork;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = HarvestCore.MODID)
public class FertilizerRender {
    //Cache Values
    public static final Cache<Fertilizer, FertilizerRenderer> CACHE = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).maximumSize(64).build();
    private static final Multimap<Fertilizer, BlockPos> fertilized = HashMultimap.create();

    public static void addBlock(Fertilizer fertilizer, BlockPos pos) {
        fertilized.get(fertilizer).add(pos);
    }

    public static void removeBlock(Fertilizer fertilizer, BlockPos pos) {
        fertilized.get(fertilizer).remove(pos);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer().world.isRemote) return;
        for (Fertilizer fertilizer : Fertilizer.REGISTRY.values()) {
            if(fertilized.get(fertilizer).remove(event.getPos())) {
                PenguinNetwork.sendToClient(new PacketRemoveFertilizer(fertilizer, event.getPos()), event.getPlayer());
            }
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (event.getWorld().isRemote) {
            PenguinNetwork.sendToServer(new PacketRequestFertilizerChunkData(ChunkPos.asLong(event.getChunk().x, event.getChunk().z)));
        }
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Load event) {
        if (event.getWorld().isRemote) {
            for (Fertilizer fertilizer : Fertilizer.REGISTRY.values()) {
                Iterator<BlockPos> it = fertilized.get(fertilizer).iterator();
                while (it.hasNext()) {
                    BlockPos pos = it.next();
                    ChunkPos chunkPos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
                    if (chunkPos.equals(event.getChunk().getPos())) {
                        it.remove();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLastRender(RenderWorldLastEvent event) throws ExecutionException {
        EntityPlayer player = Minecraft.getMinecraft().player;
        for (Fertilizer fertilizer : Fertilizer.REGISTRY.values()) {
            //if (fertilizer == ItemFertilizer.FertilizerType.NONE) continue;
            FertilizerRenderer renderer = CACHE.get(fertilizer, () ->
                    new FertilizerRenderer(new FertilizerWorldAccess(fertilizer, fertilized.get(fertilizer)), BlockPos.ORIGIN));
            if (renderer != null) {
                renderer.render(player, event.getPartialTicks());
            }
        }
    }

    //Loader
    @SubscribeEvent
    public static void TextureStitchEvent(TextureStitchEvent event) {
        for (Fertilizer fertilizer: Fertilizer.REGISTRY.values()) {
            if (!fertilizer.equals(Fertilizer.NONE)) {
                event.getMap().registerSprite(fertilizer.getBlockSprite()); //Register the block sprite
            }
        }
    }
}

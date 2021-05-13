package uk.joshiejack.penguinlib.client.renderer.item;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID, value = Side.CLIENT)
public class EntityRendererOffsets {
    private static Map<ResourceLocation, float[]> ADJUSTMENTS = Maps.newHashMap();
    private static final float[] defaults = new float[] { 0.7F, 0.5F, -0.05F, 0.5F };

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("entity_render_data").rows().forEach(row ->
                ADJUSTMENTS.put(new ResourceLocation(row.get("entity").toString()),
                        new float[] { row.getAsFloat("scale"), row.getAsFloat("x"), row.getAsFloat("y"), row.getAsFloat("z") }));
    }

    static float[] getAdjustments(Entity entity) {
        //return new float[] { 0.7F, 0.5F, -0.05F, 0.5F };//default
        //return new float[] { 0.5F, 0.5F, -0.05F, 0.5F };
        return ADJUSTMENTS.getOrDefault(EntityList.getKey(entity), defaults);
    }
}

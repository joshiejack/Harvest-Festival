package joshie.harvest.buildings.render;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.buildings.BuildingHelper;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@HFEvents(Side.CLIENT)
public class PreviewEvent {
    //Cache Values
    public static final Cache<BuildingKey, BuildingRenderer> CACHE = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).maximumSize(64).build();
    private final BuildingVertexUploader vertexUploader = new BuildingVertexUploader();
    private static final float offset = 0.00390625F;
    private ItemStack held; //Cache the held itemstack
    private Building building; //Cache the building value

    private BuildingRenderer getRenderer(World world, EntityPlayerSP player) throws ExecutionException {
        if (player == null) return null;
        ItemStack stack = player.getHeldItemMainhand();
        if (isInvalidStack(stack)) {
            stack = player.getHeldItemOffhand();
        }

        if (isInvalidStack(stack)) return null;
        else {
            if (stack != held) {
                if (stack.getItem() == HFBuildings.BLUEPRINTS) building = HFBuildings.BLUEPRINTS.getObjectFromStack(stack);
                else building = HFBuildings.STRUCTURES.getObjectFromStack(stack);
                held = stack; //Cache the held item
            }

            //Attempt the raytrace
            RayTraceResult raytrace = BuildingHelper.rayTrace(player, 128, 0F);
            if (raytrace == null || raytrace.getBlockPos() == null) return null;
            else {
                BuildingKey key = BuildingHelper.getPositioning(stack, world, raytrace, building, player, false);
                if (key != null) {
                    return CACHE.get(key, () -> new BuildingRenderer(new BuildingAccess(building, key.getRotation()), key)).setPosition(key.getPos());
                }
            }
        }

        //All else fails return null
        return null;
    }

    private boolean isBuildingItem(ItemStack stack) {
        return stack.getItem() == HFBuildings.BLUEPRINTS || stack.getItem() == HFBuildings.STRUCTURES;
    }

    private boolean isInvalidStack(ItemStack stack) {
        return stack == null || stack.getItem() == null || !isBuildingItem(stack);
    }

    private void renderRenderer(EntityPlayerSP player, BuildingRenderer renderer, float partialTick) {
        BlockPos pos = renderer.getPos();
        GlStateManager.pushMatrix();
        double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTick;
        double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTick;
        double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTick;
        GlStateManager.translate(-posX + offset, -posY + offset, -posZ + offset);
        GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
        renderer.draw(vertexUploader);
        GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
        GlStateManager.translate(posX, posY, posZ);
        GlStateManager.popMatrix();
    }

    /** Borrowed from SettlerCraft by @InfinityRaider **/
    @SubscribeEvent
    public void renderBuildingPreview(RenderWorldLastEvent event) throws ExecutionException {
        EntityPlayerSP player = MCClientHelper.getPlayer();
        BuildingRenderer renderer = getRenderer(player.worldObj, player);
        if (renderer != null) {
            renderRenderer(player, renderer, event.getPartialTicks());
        }
    }
}

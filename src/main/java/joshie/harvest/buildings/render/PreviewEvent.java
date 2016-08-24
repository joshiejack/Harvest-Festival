package joshie.harvest.buildings.render;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.generic.BuildingHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@HFEvents(Side.CLIENT)
public class PreviewEvent {
    //Cache Values
    private static final Cache<BuildingKey, BuildingRenderer> CACHE = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.MINUTES).maximumSize(128).build();
    private BuildingVertexUploader vertexUploader = new BuildingVertexUploader();
    private ItemStack held; //Cache the held itemstack
    private Building building; //Cache the building value

    private BuildingRenderer getRenderer(World world, EntityPlayerSP player) {
        if (player == null) return null;
        ItemStack stack = player.getHeldItemMainhand();
        EnumHand hand = EnumHand.MAIN_HAND;
        if (isInvalidStack(stack)) {
            stack = player.getHeldItemOffhand();
            hand = EnumHand.OFF_HAND;
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
            if (raytrace == null || raytrace.getBlockPos() == null || raytrace.sideHit != EnumFacing.UP) return null;
            else {

                try {
                    BuildingKey key = BuildingHelper.getPositioning(world, raytrace, building, player, hand);
                    return CACHE.get(key, new Callable<BuildingRenderer>() {
                        @Override
                        public BuildingRenderer call() throws Exception {
                            return new BuildingRenderer(key);
                        }
                    });
                } catch (Exception e) { return null; }
            }
        }
    }

    private boolean isBuildingItem(ItemStack stack) {
        return stack.getItem() == HFBuildings.BLUEPRINTS || stack.getItem() == HFBuildings.STRUCTURES;
    }

    private boolean isInvalidStack(ItemStack stack) {
        return stack == null || stack.getItem() == null || !isBuildingItem(stack);
    }

    /** Borrowed from SettlerCraft by @InfinityRaider **/
    @SubscribeEvent
    public void renderBuildingPreview(RenderWorldLastEvent event) {
        EntityPlayerSP player = MCClientHelper.getPlayer();
        BuildingRenderer renderer = getRenderer(player.worldObj, player);
        if (renderer != null) {
            BlockPos pos = renderer.getPos();
            GlStateManager.pushMatrix();
            float partialTick = event.getPartialTicks();
            double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTick;
            double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTick;
            double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTick;
            GlStateManager.translate(-posX, -posY, -posZ);
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
            vertexUploader.draw(renderer.getBuffer());
            GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
            GlStateManager.translate(posX, posY, posZ);
            GlStateManager.popMatrix();
        }
    }
}

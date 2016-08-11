package joshie.harvest.buildings.render;

import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.generic.BuildingHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Triple;

@HFEvents(Side.CLIENT)
public class PreviewEvent {
    private static final BuildingRenderer RENDERER = new BuildingRenderer();
    
    private boolean isBuildingItem(ItemStack stack) {
        return stack.getItem() == HFBuildings.BLUEPRINTS || stack.getItem() == HFBuildings.STRUCTURES;
    }

    //Cache Values
    private EnumHand hand;
    private ItemStack stack;
    private BlockPos pos;
    private RayTraceResult rayTraceResult;

    private boolean updateRenderer(EntityPlayerSP player) {
        if (player == null) return false;
        stack = player.getHeldItemMainhand();
        hand = EnumHand.MAIN_HAND;
        if (isInvalidStack()) {
            stack = player.getHeldItemOffhand();
            hand = EnumHand.OFF_HAND;
        }

        if (isInvalidStack()) return false;
        else {
            RENDERER.setBuilding(stack);
            return true;
        }
    }

    private BlockPos updateDirection(World world, EntityPlayerSP player) {
        RayTraceResult raytrace = BuildingHelper.rayTrace(player, 128, 0F);
        if (raytrace == null || raytrace.getBlockPos() == null || raytrace.sideHit != EnumFacing.UP) {
            return null;
        }

        //Update the preview data
        if (rayTraceResult != null && RENDERER.getBuilding() != null && raytrace.getBlockPos().equals(rayTraceResult.getBlockPos()) && RENDERER.getBuilding().equals(RENDERER.getBuilding())) return pos;
        else {
            rayTraceResult = raytrace; //Cache the value
            Triple<BlockPos, Mirror, Rotation> triple = BuildingHelper.getPositioning(world, rayTraceResult, RENDERER.getBuilding(), player, hand);
            if (triple == null) return null;
            RENDERER.setDirection(triple.getMiddle(), triple.getRight());
            pos = triple.getLeft();
            return pos;
        }
    }

    private boolean isInvalidStack() {
        return stack == null || stack.getItem() == null || !isBuildingItem(stack);
    }

    /** Borrowed from SettlerCraft by @InfinityRaider **/
    @SubscribeEvent
    public void renderBuildingPreview(RenderWorldLastEvent event) {
        EntityPlayerSP player = MCClientHelper.getPlayer();
        World world = MCClientHelper.getWorld();
        if (!updateRenderer(player)) return;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        BlockPos pos = updateDirection(world, player);
        if (pos != null) {
            GlStateManager.pushMatrix();
            float partialTick = event.getPartialTicks();
            double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTick;
            double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTick;
            double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTick;
            GlStateManager.translate(-posX, -posY, -posZ);
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
            buffer.begin(7, DefaultVertexFormats.BLOCK);
            RENDERER.render(world, buffer);
            tessellator.draw();
            GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
            GlStateManager.translate(posX, posY, posZ);
            GlStateManager.popMatrix();
        }
    }
}

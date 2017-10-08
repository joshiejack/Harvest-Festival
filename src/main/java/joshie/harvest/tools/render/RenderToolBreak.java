package joshie.harvest.tools.render;

import com.google.common.collect.ImmutableList;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.tools.HFTools;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

//Borrowed from Tinkers Construct by boni
@HFEvents(Side.CLIENT)
@SuppressWarnings("unused")
public class RenderToolBreak {
    @SubscribeEvent
    public void renderExtraBlockBreak(RenderWorldLastEvent event) {
        PlayerControllerMP controller = MCClientHelper.getPlayerController();
        EntityPlayer player = MCClientHelper.getPlayer();
        World world = player.world;
        ItemStack tool = player.getHeldItemMainhand();
        if (tool != null && (tool.getItem() == HFTools.HAMMER || tool.getItem() == HFTools.HOE)) {
            Entity renderEntity = MCClientHelper.getRenderViewEntity();
            double distance = controller.getBlockReachDistance();
            RayTraceResult rayTraceResult = renderEntity.rayTrace(distance, event.getPartialTicks());
            if (rayTraceResult != null) {
                if (tool.getItem() == HFTools.HAMMER && HFTools.HAMMER.canUse(tool)) {
                    ImmutableList<BlockPos> extraBlocks = HFTools.HAMMER.getBlocks(world, rayTraceResult.getBlockPos(), player, tool);
                    drawSelection(event.getContext(), extraBlocks, player, event.getPartialTicks());
                    if (controller.getIsHittingBlock()) {
                        drawBlockDamageTexture(MCClientHelper.getMinecraft().renderGlobal, controller,
                                Tessellator.getInstance(), Tessellator.getInstance().getBuffer(),
                                player, event.getPartialTicks(), world, extraBlocks);
                    }
                } else if (tool.getItem() == HFTools.HOE && HFTools.HOE.canUse(tool)) {
                    drawSelection(event.getContext(), HFTools.HOE.getBlocks(world, rayTraceResult.getBlockPos(), player, tool), player, event.getPartialTicks());
                }
            }
        }
    }


    private void drawSelection(RenderGlobal render, ImmutableList<BlockPos> extraBlocks, EntityPlayer player, float ticks) {
        for (BlockPos pos : extraBlocks) {
            render.drawSelectionBox(player, new RayTraceResult(new Vec3d(0, 0, 0), EnumFacing.UP, pos), 0, ticks);
        }
    }

    private void drawBlockDamageTexture(RenderGlobal renderGlobal, PlayerControllerMP controllerMP, Tessellator tessellatorIn, VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, World world, List<BlockPos> blocks) {
        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
        TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;
        int progress = (int) (controllerMP.curBlockDamageMP * 10f) - 1; // 0-10

        if(progress < 0) {
            return;
        }

        renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        renderGlobal.preRenderDamagedBlocks();
        worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation(-d0, -d1, -d2);
        worldRendererIn.noColor();

        for(BlockPos blockpos : blocks) {
            Block block = world.getBlockState(blockpos).getBlock();
            TileEntity te = world.getTileEntity(blockpos);
            boolean hasBreak = block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull;
            if(!hasBreak) {
                hasBreak = te != null && te.canRenderBreaking();
            }

            if(!hasBreak) {
                IBlockState iblockstate = world.getBlockState(blockpos);
                if(iblockstate.getMaterial() != Material.AIR) {
                    TextureAtlasSprite textureatlassprite = renderGlobal.destroyBlockIcons[progress];
                    BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                    blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, world);
                }
            }
        }

        tessellatorIn.draw();
        worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
        renderGlobal.postRenderDamagedBlocks();
    }
}

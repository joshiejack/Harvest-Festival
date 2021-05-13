package uk.joshiejack.penguinlib.client.renderer;

import com.google.common.collect.ImmutableList;
import uk.joshiejack.penguinlib.PenguinLib;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = PenguinLib.MOD_ID)
public class RenderToolBreak {
    public interface MultiRender {
        default boolean drawDamage() {
            return true;
        }

        ImmutableList<BlockPos> getPositions(EntityPlayer player, World world, BlockPos blockPos);
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void renderExtraBlockBreak(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        PlayerControllerMP controller = mc.playerController;
        EntityPlayer player = mc.player;
        World world = mc.world;
        ItemStack tool = player.getHeldItemMainhand();
        MultiRender render = tool.getItem() instanceof MultiRender ? ((MultiRender)tool.getItem()) : null;
        if (render != null && tool.getItemDamage() < tool.getMaxDamage()) {
            Entity renderEntity = mc.getRenderViewEntity();
            double distance = controller.getBlockReachDistance();
            RayTraceResult rt = renderEntity.rayTrace(distance, event.getPartialTicks());
            if (rt != null) {
                ImmutableList<BlockPos> extraBlocks = render.getPositions(player, world, rt.getBlockPos());
                drawSelection(event.getContext(), extraBlocks, player, event.getPartialTicks());
                if (render.drawDamage() && controller.getIsHittingBlock()) {
                    drawBlockDamageTexture(mc.renderGlobal, controller,
                            Tessellator.getInstance(), Tessellator.getInstance().getBuffer(),
                            player, event.getPartialTicks(), world, extraBlocks);
                }
            }
        }
    }

    private static void drawSelection(RenderGlobal render, ImmutableList<BlockPos> extraBlocks, EntityPlayer player, float ticks) {
        for (BlockPos pos : extraBlocks) {
            render.drawSelectionBox(player, new RayTraceResult(new Vec3d(0, 0, 0), EnumFacing.UP, pos), 0, ticks);
        }
    }

    private static void drawBlockDamageTexture(RenderGlobal renderGlobal, PlayerControllerMP controllerMP, Tessellator tessellatorIn, BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, World world, List<BlockPos> blocks) {
        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
        TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;
        int progress = (int) (controllerMP.curBlockDamageMP * 10f) - 1; // 0-10

        if (progress < 0) {
            return;
        }

        renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        renderGlobal.preRenderDamagedBlocks();
        worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation(-d0, -d1, -d2);
        worldRendererIn.noColor();

        for (BlockPos blockpos : blocks) {
            Block block = world.getBlockState(blockpos).getBlock();
            TileEntity te = world.getTileEntity(blockpos);
            boolean hasBreak = block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull;
            if (!hasBreak) {
                hasBreak = te != null && te.canRenderBreaking();
            }

            if (!hasBreak) {
                IBlockState iblockstate = world.getBlockState(blockpos);
                if (iblockstate.getMaterial() != Material.AIR) {
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

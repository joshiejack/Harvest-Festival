package joshie.harvest.core.helpers;

import joshie.harvest.core.network.PenguinPacketLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class MCClientHelper {
    public static Minecraft getMinecraft() {
        return FMLClientHandler.instance().getClient();
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }

    public static World getWorld() {
        return getPlayer().world;
    }

    /** Update the block at the coordinates for re-rendering **/
    public static void updateRender(BlockPos pos) {
        refresh(getDimension(), pos);
    }

    public static void refresh(int dimension, BlockPos pos) {
        if (getWorld().provider.getDimension() == dimension) {
            getWorld().markBlockRangeForRenderUpdate(pos, pos);
        }
    }

    /** Calls a for a re-render of all surrounding blocks **/
    public static void refresh() {
        getMinecraft().renderGlobal.loadRenderers();
    }

    /** Returns the dimension the player is in **/
    public static int getDimension() {
        return getWorld().provider.getDimension();
    }

    public static TileEntity getTile(PenguinPacketLocation message) {
        return getWorld().getTileEntity(message.pos);
    }

    public static void initGui() {
        if (getMinecraft().currentScreen != null) {
            getMinecraft().currentScreen.initGui();
        }
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static Entity getRenderViewEntity() {
        return Minecraft.getMinecraft().getRenderViewEntity();
    }

    public static boolean isClient(EntityLivingBase playerIn) {
        return playerIn == getPlayer();
    }
}
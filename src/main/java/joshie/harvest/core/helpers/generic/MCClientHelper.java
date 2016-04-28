package joshie.harvest.core.helpers.generic;

import joshie.harvest.core.network.AbstractPacketLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class MCClientHelper {
    public static Minecraft getMinecraft() {
        return FMLClientHandler.instance().getClient();
    }

    public static EntityPlayer getPlayer() {
        return getMinecraft().thePlayer;
    }

    public static World getWorld() {
        return getPlayer().worldObj;
    }

    /** Update the block at the coordinates for re-rendering **/
    public static void updateRender(BlockPos pos) {
        refresh(getDimension(), pos);
    }

    public static void refresh(int dimension, BlockPos pos) {
        if (getWorld().provider.getDimension() == dimension) {
            getWorld().markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }
    }

    /** Calls a for a re-render of all surrounding blocks **/
    public static void refresh() {
        EntityPlayer player = getPlayer();
        getMinecraft().renderGlobal.markBlockRangeForRenderUpdate((int) player.posX - 176, 0, (int) player.posZ - 176, (int) player.posX + 176, 256, (int) player.posZ + 176);
    }

    /** Returns the dimension the player is in **/
    public static int getDimension() {
        return getWorld().provider.getDimension();
    }

    /** Add text to the game chat **/
    public static void addToChatAndTranslate(String str) {
        getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(str, 0));
    }

    /** Add text to the game chat **/
    public static void addToChat(String str) {
        getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(str));
    }

    //Mouse Helper
    public static Point getMouse(GuiContainer container) {
        Minecraft mc = getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);
        Dimension size = new Dimension(res.getScaledWidth(), res.getScaledHeight());
        Dimension resolution = new Dimension(mc.displayWidth, mc.displayHeight);
        Point mousepos = new Point(Mouse.getX() * size.width / resolution.width, size.height - Mouse.getY() * size.height / resolution.height - 1);
        int guiLeft = (container.width - container.xSize) / 2;
        int guiTop = (container.height - container.ySize) / 2;
        return new Point(mousepos.x - guiLeft, mousepos.y - guiTop);
    }

    public static TileEntity getTile(AbstractPacketLocation message) {
        return getWorld().getTileEntity(message.pos);
    }
}
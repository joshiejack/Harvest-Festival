package harvestmoon.helpers;

import java.awt.Dimension;
import java.awt.Point;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientHelper {
    @SideOnly(Side.CLIENT)
    public static World getWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @SideOnly(Side.CLIENT)
    public static int getDimension() {
        return getWorld().provider.dimensionId;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @SideOnly(Side.CLIENT)
    public static void refresh() {
        EntityPlayer player = getPlayer();
        Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate((int) player.posX - 176, 0, (int) player.posZ - 176, (int) player.posX + 176, 256, (int) player.posZ + 176);
    }

    @SideOnly(Side.CLIENT)
    public static Point getMouse(GuiContainer container) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        Dimension size = new Dimension(res.getScaledWidth(), res.getScaledHeight());
        Dimension resolution = new Dimension(mc.displayWidth, mc.displayHeight);
        Point mousepos = new Point(Mouse.getX() * size.width / resolution.width, size.height - Mouse.getY() * size.height / resolution.height - 1);
        int guiLeft = (container.width - container.xSize) / 2;
        int guiTop = (container.height - container.ySize) / 2;
        Point relMouse = new Point(mousepos.x - guiLeft, mousepos.y - guiTop);
        return relMouse;
    }

    @SideOnly(Side.CLIENT)
    public static void refresh(int dimension, int x, int y, int z) {
        if (getWorld().provider.dimensionId == dimension) {
            getWorld().markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }
    }
}

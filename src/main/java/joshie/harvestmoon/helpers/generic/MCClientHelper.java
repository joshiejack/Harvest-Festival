package joshie.harvestmoon.helpers.generic;

import java.awt.Dimension;
import java.awt.Point;

import joshie.harvestmoon.network.AbstractPacketLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

    /** Client Side get Held Item **/
    public static ItemStack getHeldItem() {
        return getPlayer().getCurrentEquippedItem();
    }

    /** Update the block at the coordinates for re-rendering **/
    public static void updateRender(int x, int y, int z) {
        EntityPlayer player = getPlayer();
        refresh(getDimension(), x, y, z);
    }

    public static void refresh(int dimension, int x, int y, int z) {
        if (getWorld().provider.dimensionId == dimension) {
            getWorld().markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }
    }

    /** Calls a for a re-render of all surrounding blocks **/
    public static void refresh() {
        EntityPlayer player = getPlayer();
        getMinecraft().renderGlobal.markBlockRangeForRenderUpdate((int) player.posX - 176, 0, (int) player.posZ - 176, (int) player.posX + 176, 256, (int) player.posZ + 176);
    }

    /** Returns the dimension the player is in **/
    public static int getDimension() {
        return getWorld().provider.dimensionId;
    }

    /** Whethr in focus or not **/
    public static boolean inFocus() {
        return getMinecraft().inGameHasFocus;
    }

    /** Add text to the game chat **/
    public static void addToChatAndTranslate(String str) {
        getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation(str, new Object[0]));
    }

    /** Add text to the game chat **/
    public static void addToChat(String str) {
        getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(str));
    }

    //Keybindings
    public static boolean isSneakingPressed() {
        return GameSettings.isKeyDown(getMinecraft().gameSettings.keyBindSneak);
    }

    public static boolean isSprintingPressed() {
        return GameSettings.isKeyDown(getMinecraft().gameSettings.keyBindSprint);
    }

    public static boolean isForwardPressed() {
        return GameSettings.isKeyDown(getMinecraft().gameSettings.keyBindForward);
    }

    public static boolean isJumpPressed() {
        return GameSettings.isKeyDown(getMinecraft().gameSettings.keyBindJump);
    }

    public static boolean isShiftPressed() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    //Mouse Helper
    public static Point getMouse(GuiContainer container) {
        Minecraft mc = getMinecraft();
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        Dimension size = new Dimension(res.getScaledWidth(), res.getScaledHeight());
        Dimension resolution = new Dimension(mc.displayWidth, mc.displayHeight);
        Point mousepos = new Point(Mouse.getX() * size.width / resolution.width, size.height - Mouse.getY() * size.height / resolution.height - 1);
        int guiLeft = (container.width - container.xSize) / 2;
        int guiTop = (container.height - container.ySize) / 2;
        Point relMouse = new Point(mousepos.x - guiLeft, mousepos.y - guiTop);
        return relMouse;
    }

    //Binds a texture
    public static void bindTexture(ResourceLocation texture) {
        getMinecraft().getTextureManager().bindTexture(texture);
    }

    //returns the lang currently in use
    public static String getLang() {
        return FMLClientHandler.instance().getCurrentLanguage();
    }

    public static TileEntity getTile(AbstractPacketLocation message) {
        return getWorld().getTileEntity(message.x, message.y, message.z);
    }
}

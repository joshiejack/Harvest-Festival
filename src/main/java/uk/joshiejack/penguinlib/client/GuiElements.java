package uk.joshiejack.penguinlib.client;

import net.minecraft.util.ResourceLocation;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

public class GuiElements {
    public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(MOD_ID, "textures/gui/shadow.png");
    public static final ResourceLocation BOOK_LEFT = new ResourceLocation(MOD_ID, "textures/gui/book_left.png");
    public static final ResourceLocation BOOK_RIGHT = new ResourceLocation(MOD_ID, "textures/gui/book_right.png");
    public static final ResourceLocation ICONS = GuiElements.getTexture("minecraft", "icons");

    public static ResourceLocation getTexture(String domain, String path) {
        return new ResourceLocation(domain, "textures/gui/" + path + ".png");
    }
}

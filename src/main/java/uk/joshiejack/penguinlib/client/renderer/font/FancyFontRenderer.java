package uk.joshiejack.penguinlib.client.renderer.font;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import it.unimi.dsi.fastutil.chars.CharSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.PenguinLib;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class FancyFontRenderer {
    private static final FontRenderer INSTANCE;
    private static final CharSet ACCEPTED = new CharOpenHashSet();

    static {
        Minecraft mc = Minecraft.getMinecraft();
        INSTANCE = new FontRenderer(mc.gameSettings, new ResourceLocation(PenguinLib.MOD_ID, "textures/font/ascii.png"), mc.renderEngine, false) {
            @Override
            protected void setColor(float r, float g, float b, float a) {}
        };

        if (mc.getLanguageManager() != null) {
            INSTANCE.setUnicodeFlag(mc.fontRenderer.getUnicodeFlag());
            INSTANCE.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }

        ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(INSTANCE);
        ACCEPTED.addAll(Lists.newArrayList(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', ',', '.', "é".charAt(0), '\''
        ));
    }

    private static boolean renderAsVanilla(GuiScreen gui, String text, int x, int y) {
        GlStateManager.pushMatrix();
        float scale = 1.5F;
        GlStateManager.scale(scale, scale, scale);
        gui.drawString(gui.mc.fontRenderer, text, (int) ((x) / scale), (int) ((y) / scale), 0xA18B61);
        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.popMatrix();
        return false;
    }

    private static boolean renderAsHF(String text, int x, int y, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        INSTANCE.drawString(text, (int) ((x) / scale), (int) ((y) / scale), 0);
        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.popMatrix();
        return true;
    }

    public static boolean render(GuiScreen gui, int x, int y, String text, float scale) {
        char[] characters = text.toCharArray();
        List<Character> list = new ArrayList<>();
        for (char c: characters) list.add(c);
        if (!ACCEPTED.containsAll(list)) return renderAsVanilla(gui, text, x, y);
        return renderAsHF( text, x, y, scale);
    }

    public static boolean render(GuiScreen gui, int x, int y, String text, boolean rightAligned) {
        char[] characters = text.toCharArray();
        List<Character> list = new ArrayList<>();
        for (char c: characters) list.add(c);
        if (!ACCEPTED.containsAll(list)) return renderAsVanilla(gui, text, x, y);
        if (rightAligned) {
            x -= INSTANCE.getStringWidth(text);
            y++;
        } else {
            x += 5;
            y += 3;
        }

        return renderAsHF( text, x, y, rightAligned ? 1F : 1.1F);
    }
}
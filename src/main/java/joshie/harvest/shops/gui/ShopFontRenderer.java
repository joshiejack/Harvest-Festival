package joshie.harvest.shops.gui;

import gnu.trove.set.TCharSet;
import gnu.trove.set.hash.TCharHashSet;
import joshie.harvest.npc.gui.GuiNPCBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@SuppressWarnings("ConstantConditions")
public class ShopFontRenderer {
    private static final FontRenderer INSTANCE;
    private static final TCharSet ACCEPTED = new TCharHashSet();

    static {
        Minecraft mc = Minecraft.getMinecraft();
        INSTANCE = new FontRenderer(mc.gameSettings, new ResourceLocation(MODID, "textures/font/ascii.png"), mc.renderEngine, false) {
            @Override
            protected void setColor(float r, float g, float b, float a) {}
        };

        if (mc.getLanguageManager() != null) {
            INSTANCE.setUnicodeFlag(mc.fontRendererObj.getUnicodeFlag());
            INSTANCE.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }

        ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(INSTANCE);
        ACCEPTED.addAll(new char[] {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', ',', '.', "é".charAt(0), '\''
        });
    }

    private static boolean renderAsVanilla(GuiNPCBase gui, String text, int x, int y) {
        GlStateManager.pushMatrix();
        float scale = 1.5F;
        GlStateManager.scale(scale, scale, scale);
        gui.drawString(gui.getFont(), text, (int) ((x) / scale), (int) ((y) / scale), 0xA18B61);
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

    public static boolean render(GuiNPCBase gui, int x, int y, String text, boolean rightAligned) {
        char[] characters = text.toCharArray();
        if (!ACCEPTED.containsAll(characters)) return renderAsVanilla(gui, text, x, y);
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
package joshie.harvest.core.util;

import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.hash.TCharObjectHashMap;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.npc.gui.GuiNPCBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ChatFontRenderer {
    private enum ChatChar {
        a('a', 2), b('b', 14), c('c', 26), d('d', 38), e('e', 50), f('f', 62, 9), g('g', 72), h('h', 84),
        i('i', 96, 6), j('j', 103, 7), k('k', 112, 11), l('l', 125, 6), m('m', 133, 14), n('n', 149),
        o('o', 161), p('p', 173), q('q', 185), r('r', 197), s('s', 209), t('t', 221, 9), u('u', 232),
        v('v', 2, 25, 11), w('w', 14, 25, 14), x('x', 30, 25, 11), y('y', 42, 25, 11), z('z', 54, 25, 10),

        A('A', 2, 93, 11), B('B', 15, 93, 10), C('C', 27, 93, 10), D('D', 39, 93, 10), E('E', 51, 93, 9),
        F('F', 62, 93, 9), G('G', 73, 93, 10), H('H', 85, 93, 10), I('I', 97, 93, 6), J('J', 105, 93, 10),
        K('K', 117, 93, 11), L('L', 130, 93, 9), M('M', 141, 93, 14), N('N', 157, 93, 11), O('O', 170, 93, 10),
        P('P', 182, 93, 10), Q('Q', 194, 93, 10), R('R', 206, 93, 11), S('S', 219, 93, 10), T('T', 231, 93, 10),
        U('U', 243, 93, 10), V('V', 2, 116, 11), W('W', 15, 116, 15), X('X', 32, 116, 12),
        Y('Y', 46, 116, 12), Z('Z', 59, 116, 10), SPACE(' ', 240, 240, 6);

        private final char character;
        private final int xPosition;
        private final int yPosition;
        private final int yPosition2;
        private final int width;

        ChatChar(char c, int x, int y, int w) {
            this.character = c;
            this.xPosition = x;
            this.yPosition = y;
            this.yPosition2 = y + 45;
            this.width = w;
        }

        ChatChar(char c, int x, int w) {
            this(c, x, 2, w);
        }

        ChatChar(char c, int x) {
            this(c, x, 10);
        }

        public char getCharacter() {
            return character;
        }

        public int getXPosition() {
            return xPosition;
        }

        public int getYPosition() {
            return yPosition;
        }

        public int getYPosition2() {
            return yPosition2;
        }

        public int getWidth() {
            return width;
        }
    }

    private static final ResourceLocation resource = new ResourceLocation(HFModInfo.MODID, "textures/gui/chattext.png");
    private static final TCharObjectMap<ChatChar> map = new TCharObjectHashMap<>();

    static {
        for (ChatChar c : ChatChar.values()) {
            map.put(c.getCharacter(), c);
        }
    }

    public static void colorise(int color) {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        GlStateManager.color(red, green, blue, 1F);
    }

    private static int getWidth(char c) {
        if (!map.containsKey(c)) return -1;
        if (map.get(c) != null) return map.get(c).getWidth();
        else return 10;
    }

    private static boolean renderAsVanilla(GuiNPCBase gui, String text, int x, int y, int colorInner) {
        GlStateManager.pushMatrix();
        float scale = 1.5F;
        GlStateManager.scale(scale, scale, scale);
        gui.drawString(gui.getFont(), text, (int) ((x + 235 - (gui.getFont().getStringWidth(text) * scale)) / scale), (int) ((y + 137) / scale), colorInner);
        GlStateManager.popMatrix();
        return false;
    }

    public static boolean render(GuiNPCBase gui, int x, int y, String text, int colorInner, int colorOuter) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
        char[] characters = text.toCharArray();
        int offset = 0;
        int width = 0;
        int xOffset = 235;
        int yOffset = 134;
        for (char c : characters) {
            int extraWidth = getWidth(c) - 2;
            if (extraWidth <= 0) {
                return renderAsVanilla(gui, text, x, y, colorInner);
            } else width += extraWidth;
        }

        for (char c : characters) {
            ChatChar cNum = map.get(c);
            if (cNum != null) {
                colorise(colorInner);
                gui.drawTexturedModalRect(x + offset - width + xOffset, y + yOffset, cNum.getXPosition(), cNum.getYPosition(), cNum.getWidth(), 19);
                colorise(colorOuter);
                gui.drawTexturedModalRect(x + offset - width + xOffset, y + yOffset, cNum.getXPosition(), cNum.getYPosition2(), cNum.getWidth(), 19);
                offset += cNum.getWidth() - 2;
            }
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        return true;
    }
}
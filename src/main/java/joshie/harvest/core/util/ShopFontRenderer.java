package joshie.harvest.core.util;

import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.hash.TCharObjectHashMap;
import joshie.harvest.npc.gui.GuiNPCBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ShopFontRenderer {
    private enum ShopChar {
        A('A', 0, 8, 7), B('B', 8, 8, 5), C('C', 27, 156, 11), D('D', 39, 156, 13), E('E', 53, 156, 11), F('F', 65, 156, 10), G('G', 76, 156, 12),
        H('H', 89, 156, 14), I('I', 104, 156, 7), J('J', 112, 156, 9), K('K', 122, 156, 12), L('L', 135, 156, 12), M('M', 148, 156, 15), N('N', 164, 156, 15),
        O('O', 179, 156, 13), P('P', 193, 156, 11), Q('Q', 1, 175, 13), R('R', 15, 175, 13), S('S', 29, 175, 9), T('T', 39, 175, 12),
        U('U', 52, 175, 13), V('V', 66, 175, 12), W('W', 79, 175, 15), X('X', 95, 175, 12), Y('Y', 108, 175, 13), Z('Z', 122, 175, 11),

        a('a', 1, 194, 8), b('b', 10, 194, 9), c('c', 20, 194, 9), d('d', 30, 194, 10), e('e', 41, 194, 8), f('f', 50, 194, 8), g('g', 59, 194, 10),
        h('h', 70, 194, 10), i('i', 81, 194, 6), j('j', 88, 194, 8), k('k', 97, 194, 10), l('l', 108, 194, 5),  m('m', 114, 194, 14),
        n('n', 129, 194, 10), o('o', 140, 194, 9), p('p', 150, 194, 10), q('q', 161, 194, 10), r('r', 172, 194, 7),  s('s', 180, 194, 7),
        t('t', 188, 194, 7), u('u', 1, 213, 10),  v('v', 12, 213, 8), w('w', 21, 213, 12), x('x', 34, 213, 11), y('y', 46, 213, 12), z('z', 59, 213, 9),

        SPACE(" ".charAt(0), 240, 240, 6), COMMA(",".charAt(0), 95, 232, 4), DOT(".".charAt(0), 100, 232, 4), ACCUTEE("é".charAt(0), 69, 213, 8), APOSTROPHE("'".charAt(0), 105, 232, 4),

        ZERO('0', 1, 232, 9), ONE('1', 11, 232, 7), TWO('2', 19, 232, 9), THREE('3', 29, 232, 8), FOUR('4', 38, 232, 9),
        FIVE('5', 48, 232, 8),SIX('6', 57, 232, 9), SEVEN('7', 67, 232, 8), EIGHT('8', 76, 232, 8), NINE('9', 85, 232, 9);

        private final char character;
        private final int xPosition;
        private final int yPosition;
        private final int width;

        ShopChar(char c, int x, int y, int w) {
            this.character = c;
            this.xPosition = x;
            this.yPosition = y;
            this.width = w;
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

        public int getWidth() {
            return width;
        }
    }

    private static final ResourceLocation resource = new ResourceLocation(MODID, "textures/gui/shopstext.png");
    private static final TCharObjectMap<ShopChar> map = new TCharObjectHashMap<>();

    static {
        for (ShopChar c : ShopChar.values()) {
            map.put(c.getCharacter(), c);
        }
    }

    private static int getWidth(char c) {
        if (!map.containsKey(c)) return -1;
        if (map.get(c) != null) return map.get(c).getWidth();
        else return 10;
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

    public static boolean render(GuiNPCBase gui, int x, int y, String text, boolean rightAligned) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
        char[] characters = text.toCharArray();
        int offset = 0;
        int width = 0;
        for (char c : characters) {
            int extraWidth = getWidth(c) - 2;
            if (extraWidth <= 0) {
                return renderAsVanilla(gui, text, x, y);
            } else width += extraWidth;
        }

        if (rightAligned) offset = -width;
        for (char c : characters) {
            ShopChar cNum = map.get(c);
            if (cNum != null) {
                gui.drawTexturedModalRect(x + offset, y, cNum.getXPosition(), cNum.getYPosition(), cNum.getWidth(), 18);
                offset += cNum.getWidth() - 2;
            }
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        return true;
    }
}
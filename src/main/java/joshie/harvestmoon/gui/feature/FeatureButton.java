package joshie.harvestmoon.gui.feature;

import java.util.List;

import joshie.harvestmoon.gui.GuiBase;
import joshie.harvestmoon.util.Translate;

import org.lwjgl.opengl.GL11;

public class FeatureButton extends Feature {
    public Button[] buttons;

    public FeatureButton(Button... button) {
        buttons = button;
    }

    @Override
    public void addTooltip(List tooltip, int mouseX, int mouseY) {
        int pos = 0;
        for (Button note : buttons) {
            if (mouseX >= -21 && mouseX <= 0 && mouseY >= 8 + 23 * pos && mouseY <= 8 + 23 * pos + 21) {
                tooltip.add(joshie.lib.util.Text.RED + Translate.translate("tab." + note.unlocalized));
                for (int i = 0; i < 5; i++) {
                    addLine("notification", i, note.toString(), tooltip);
                }
            }

            pos++;
        }
    }

    @Override
    public void draw(GuiBase gui, int x, int y, int mouseX, int mouseY) {
        super.draw(gui, x, y, mouseX, mouseY);

        int pos = 0;
        for (Button note : buttons) {
            float red = (note.color >> 16 & 255) / 255.0F;
            float green = (note.color >> 8 & 255) / 255.0F;
            float blue = (note.color & 255) / 255.0F;

            GL11.glColor4f(red, green, blue, 1.0F);
            gui.drawTexturedModalRect(x - 21, y + 8 + 23 * pos, 176, 99, 21, 22);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);

            gui.drawTexturedModalRect(x - 21 + 3, y + 8 + 2 + 23 * pos, note.x, note.y, 18, 18);

            pos++;
        }
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        int pos = 0;
        for (Button note : buttons) {
            if (mouseX >= -21 && mouseX <= 0 && mouseY >= 8 + 23 * pos && mouseY <= 8 + 23 * pos + 21) {
                note.var = note.val;
            }
            
            pos++;
        }
    }

    public static class Button {
        public String unlocalized;
        public Object var;
        public Object val;
        private int x;
        private int y;
        private int color;

        public Button(String unlocalized, int x, int y, int color, Object variable, Object value) {
            this.unlocalized = unlocalized;
            this.var = variable;
            this.val = value;
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
}

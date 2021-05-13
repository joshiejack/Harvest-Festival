package uk.joshiejack.gastronomy.client.gui.labels;

import uk.joshiejack.gastronomy.GastronomyConfig;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LabelIntroduction extends LabelBook {
    private final String suffix;
    public LabelIntroduction(GuiBook gui) {
        super(gui, 0, 0);
        this.suffix = GastronomyConfig.findRecipes ? ".loot" : ".free";
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            String localized = StringHelper.localize("gastronomy.item.cookbook.name");
            int left = ((36-localized.length()) * 2);
            mc.fontRenderer.drawString(TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + localized, x + left, y + 20, 4210752);
            drawHorizontalLine(x + 25, x + 135, y + 30, 0xFFB0A483);
            drawHorizontalLine(x + 26, x + 136, y + 31, 0xFF9C8C63);
            mc.fontRenderer.drawSplitString(StringEscapeUtils.unescapeJava(StringHelper.localize("gastronomy.cookbook.intro" + suffix)), x + 25, y + 35, 120, 4210752);
        }
    }
}

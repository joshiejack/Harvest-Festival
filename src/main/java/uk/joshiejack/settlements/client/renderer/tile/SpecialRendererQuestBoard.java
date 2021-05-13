package uk.joshiejack.settlements.client.renderer.tile;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.settlements.quest.settings.QuestBoardClient;
import uk.joshiejack.settlements.tile.TileQuestBoard;
import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRendererItem;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
@PenguinLoader(value = "quest_board", side = Side.CLIENT)
public class SpecialRendererQuestBoard extends SpecialRendererItem<TileQuestBoard> {
    @Override
    public void render(@Nonnull TileQuestBoard te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (QuestBoardClient.hasDailyQuest()) {
            GlStateManager.pushMatrix();
            float div = 2.375F;
            GlStateManager.translate((float) x + 0.5F - (te.getFacing().getXOffset() / div),
                    (float) y + 0.25F, (float) z + 0.5F - (te.getFacing().getZOffset() / div));
            float f1 = te.getFacing().getHorizontalAngle();

            GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
            GlStateManager.enableRescaleNormal();
            FontRenderer fontrenderer = this.getFontRenderer();
            GlStateManager.translate(0.0F, 0.33333334F, 0.046666667F);
            GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
            GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
            GlStateManager.depthMask(false);
            ITextComponent[] signText = QuestBoardClient.getNextQuest().getValue().getTitle();
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            for (int j = 0; j < signText.length; ++j) {
                TextFormatting formatting = j == 0 ? TextFormatting.ITALIC : j == 1 ? TextFormatting.BOLD : TextFormatting.RESET;
                ITextComponent itextcomponent = signText[j];
                List<ITextComponent> list = GuiUtilRenderComponents.splitText(itextcomponent, 90, fontrenderer, false, true);
                String s = !list.isEmpty() ? list.get(0).getFormattedText() : "";
                fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, (j * 10 - signText.length * 5), 0);
            }

            GlStateManager.depthMask(true);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }
}

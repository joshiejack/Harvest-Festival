package uk.joshiejack.furniture.client.gui;

import com.google.common.collect.Lists;
import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.furniture.client.gui.button.ButtonChannel;
import uk.joshiejack.furniture.television.TVChannel;
import uk.joshiejack.furniture.tile.TileTelevision;
import uk.joshiejack.penguinlib.client.gui.GuiPenguin;
import uk.joshiejack.penguinlib.client.renderer.font.FancyFontRenderer;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class GuiTelevision extends GuiPenguin {
    private static final ResourceLocation TEXTURE = GuiElements.getTexture(Furniture.MODID, "television");
    public final TileTelevision television;

    public GuiTelevision(TileTelevision television) {
        super(TEXTURE);
        this.xSize = 234;
        this.ySize = 132;
        this.television = television;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        int x = 0;
        int y = 0;
        List<TVChannel> reversed = Lists.newArrayList(TVChannel.REGISTRY.values());
        Collections.reverse(reversed);
        for (TVChannel channel: reversed) {
            if (channel.isSelectable()) {
                buttonList.add(new ButtonChannel(this, buttonList.size(), guiLeft + 16 + x, guiTop + 28 + y, channel));
                x += 70;

                if (x > 140) {
                    x = 0;
                    y += 54;
                }
            }
        }
    }

    @Override
    protected void drawGuiTexture() {
        if (texture != null) {
            mc.renderEngine.bindTexture(texture);
            int rows = 1 + (int) ((double)buttonList.size() / 3);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 76);
            for (int i = 0; i < rows - 1; i++) {
                drawTexturedModalRect(guiLeft, guiTop + 76 + i * 76, 0, 22, xSize, 58);
            }

            drawTexturedModalRect(guiLeft, guiTop + (rows * 67), 0, 80, xSize, 2);
        }
    }

    @Override
    public void drawForeground(int x, int y) {
        String text = "Select Channel";
        int middle = (xSize / 2) - fontRenderer.getStringWidth(text) / 2;
        FancyFontRenderer.render(this, middle, 6, "Select Channel", false);
    }
}

package uk.joshiejack.harvestcore.client.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.client.gui.button.ButtonFloor;
import uk.joshiejack.penguinlib.client.gui.GuiPenguin;
import uk.joshiejack.penguinlib.client.GuiElements;

public class GuiElevator extends GuiPenguin {
    public static final ResourceLocation ELEVATOR = GuiElements.getTexture(HarvestCore.MODID, "elevator");
    private final int maxFloor;

    public GuiElevator(int maxFloor) {
        super(ELEVATOR);
        this.maxFloor = maxFloor;
        this.xSize = 227;
        this.ySize = 218;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        if (maxFloor > 5) {
            int y = 0;
            int i = 0;
            int index = 0;
            while (index <= 24) {
                int floor = (index * 5) + 1;
                if (floor > maxFloor) break; //Exit early
                buttonList.add(new ButtonFloor(index, guiLeft + 35 + (y * 32), guiTop + 28 + (i * 22), floor));
                y++;

                if (y > 4) {
                    y = 0;
                    i++;
                }

                index++;
            }
        }
    }

    @Override
    protected void drawGuiTexture() {
        mc.renderEngine.bindTexture(texture);
        int yHeight = ((buttonList.size() - 1) / 5) + 1;
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 24);
       // yHeight = 1;
        for (int y = 0; y < yHeight; y++) {
            drawTexturedModalRect(guiLeft, guiTop + 24 + (y * 23), 0, 25, xSize, 24);
        }

        drawTexturedModalRect(guiLeft, guiTop + 16 + (yHeight * 23), 0, 134, xSize, 24);
        if (buttonList.size() == 0) {
            String text = "No elevators located!";
            int width = fontRenderer.getStringWidth(text) / 2;
            fontRenderer.drawString(TextFormatting.BOLD + text, guiLeft - width + (xSize / 2) - 10, guiTop + 28, 4210752);
        }
    }
}

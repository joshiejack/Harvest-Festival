package uk.joshiejack.harvestcore.client.gui.button;

import it.unimi.dsi.fastutil.chars.Char2IntMap;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import uk.joshiejack.harvestcore.client.gui.GuiElevator;
import uk.joshiejack.harvestcore.network.mine.PacketElevator;
import uk.joshiejack.harvestcore.world.mine.MineHelper;
import uk.joshiejack.penguinlib.network.PenguinNetwork;

import javax.annotation.Nonnull;

public class ButtonFloor extends GuiButton {
    private static final Char2IntMap charToX = new Char2IntOpenHashMap();
    static {
        for (int i = 0; i <= 9; i++) {
            charToX.put(("" + i).toCharArray()[0], i * 8);
        }
    }

    private final int floor;
    private final char[] floorText;
    public ButtonFloor(int buttonId, int x, int y, int floor) {
        super(buttonId, x, y, "");
        this.floor = floor;
        this.floorText = ("" + floor).toCharArray();
        this.width = 28;
        this.height = 18;
        this.enabled = MineHelper.getFloorFromEntity(Minecraft.getMinecraft().player) != floor;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            int theFloor = MineHelper.getFloorFromEntity(mc.player);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            mc.getTextureManager().bindTexture(GuiElevator.ELEVATOR);
            int theY = theFloor == floor ? 175: hovered ? 211: 193;
            drawTexturedModalRect(x, y, 0, theY, width, height);
            int x = 0;
            int offset = floor < 10 ? 10 : floor < 100 ? 5 : 0;
            for (char c: floorText) {
                x += 9;
                drawTexturedModalRect(this.x + x + offset - 7, y + 5, charToX.get(c), 249, 8, 8);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (MineHelper.getFloorFromEntity(Minecraft.getMinecraft().player) != floor) {
            Minecraft.getMinecraft().player.closeScreen();
            PenguinNetwork.sendToServer(new PacketElevator(floor));
        }
    }
}

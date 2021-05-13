package uk.joshiejack.settlements.client.gui;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.client.gui.Chatter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiNPCChat extends GuiNPC {
    private final Chatter script;
    private final String[] objects;

    public GuiNPCChat(EntityNPC npc, Chatter chatter, String... objects) {
        super(npc);
        this.script = chatter.withWidth(220);
        this.objects = objects;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        script.update(fontRenderer, objects);
    }

    @Override
    protected void drawOverlay(int x, int y) {
        script.draw(fontRenderer, 20, (sr.getScaledHeight() / 2) + 32, 0xFFFFFF);
    }

    private void skip(int button) {
        if (selectedButton == null && script.mouseClicked(button)) {
            mc.player.closeScreen();
        }
    }

    @Override
    public void keyTyped(char character, int key) throws IOException {
        super.keyTyped(character, key);
        //Enter or Spacebar or Q
        if (key == 28 || key == 57 || character == 'q') {
            skip(0); //Forwads
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        super.mouseClicked(x, y, mouseButton);
        skip(mouseButton);
    }
}

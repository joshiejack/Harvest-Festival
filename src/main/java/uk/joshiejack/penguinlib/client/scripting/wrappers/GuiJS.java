package uk.joshiejack.penguinlib.client.scripting.wrappers;

import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import net.minecraft.client.gui.Gui;

@SuppressWarnings("unused")
public class GuiJS<G extends Gui> extends AbstractJS<G> {
    public GuiJS(G gui) {
        super(gui);
    }
}

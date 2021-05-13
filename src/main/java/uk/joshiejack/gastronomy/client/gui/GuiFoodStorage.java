package uk.joshiejack.gastronomy.client.gui;

import uk.joshiejack.gastronomy.inventory.slot.SlotFood;
import uk.joshiejack.penguinlib.client.gui.GuiPenguinContainer;
import uk.joshiejack.penguinlib.client.gui.MicroFont;
import uk.joshiejack.penguinlib.inventory.ContainerPenguinInventory;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GuiFoodStorage extends GuiPenguinContainer {
    public GuiFoodStorage(ContainerPenguinInventory container, ResourceLocation texture, int offsetY) {
        super(container, texture, offsetY);
    }

    @Override
    public void drawSlot(@Nonnull Slot slot) {
        if (slot instanceof SlotFood) {
            FontRenderer temp = fontRenderer;
            fontRenderer = MicroFont.INSTANCE;
            super.drawSlot(slot);
            fontRenderer = temp;
        } else super.drawSlot(slot);
    }
}

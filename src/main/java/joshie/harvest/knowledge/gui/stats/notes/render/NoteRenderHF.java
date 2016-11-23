package joshie.harvest.knowledge.gui.stats.notes.render;

import joshie.harvest.api.knowledge.NoteRender;
import joshie.harvest.core.helpers.StackRenderHelper;
import net.minecraft.item.ItemStack;

import java.util.Random;

public abstract class NoteRenderHF extends NoteRender {
    public static final Random rand = new Random();

    protected void drawStack(ItemStack stack, int x, int y) {
        StackRenderHelper.drawStack(stack, guiLeft + x, guiTop + y, 1F);
    }
}

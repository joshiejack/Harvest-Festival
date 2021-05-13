package uk.joshiejack.penguinlib.client.scripting.wrappers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import uk.joshiejack.penguinlib.client.gui.CyclingStack;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;

public class BookLabelJS extends GuiJS<LabelBook> {
    public BookLabelJS(LabelBook label) {
        super(label);
    }

    public void drawStack(ItemStackJS stack, int x, int y, float scale) {
        penguinScriptingObject.drawStack(stack.penguinScriptingObject, x, y, scale);
    }

    public void drawCyclingStack(CyclingStack stack, int x, int y, float scale) {
        penguinScriptingObject.drawStack(stack.getStack(0), x, y, scale);
    }

    public void drawGold(long gold, int x, int y) {
        /*
        LabelBook object = penguinScriptingObject;
        boolean unicode = object.gui.mc.fontRenderer.getUnicodeFlag();
        object.gui.mc.fontRenderer.setUnicodeFlag(true);
        GlStateManager.color(1F, 1F, 1F);
        String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(gold);
        object.gui.mc.getTextureManager().bindTexture(GuiShop.EXTRA);
        object.drawTexturedModalRect(object.x + x, object.y + y, 244, 244, 12, 12);
        object.gui.mc.fontRenderer.drawString(TextFormatting.BOLD + text, object.x + x + 15, object.y + y + 2, 0x857754);
        object.gui.mc.fontRenderer.setUnicodeFlag(unicode); */
    }

    public void drawLeftArrow(int x, int y) {
        LabelBook object = penguinScriptingObject;
        GlStateManager.color(1F, 1F, 1F); //Fix colours
        object.gui.mc.getTextureManager().bindTexture(object.gui.left);
        object.drawTexturedModalRect(object.x + x, object.y + y, 16, 235, 15, 10);
    }

    public void drawRightArrow(int x, int y) {
        LabelBook object = penguinScriptingObject;
        GlStateManager.color(1F, 1F, 1F); //Fix colours
        object.gui.mc.getTextureManager().bindTexture(object.gui.left);
        object.drawTexturedModalRect(object.x + x, object.y + y, 0, 235, 15, 10);
    }

    public CyclingStack createCyclingStack(NonNullList<ItemStackJS> stacks) {
        NonNullList<ItemStack> list = NonNullList.withSize(stacks.size(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            list.set(i, stacks.get(i).penguinScriptingObject);
        }

        return new CyclingStack(list);
    }
}

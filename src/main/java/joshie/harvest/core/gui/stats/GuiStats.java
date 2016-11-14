package joshie.harvest.core.gui.stats;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.base.gui.GuiBaseBook;
import joshie.harvest.core.gui.stats.button.ButtonTabLeft;
import joshie.harvest.core.gui.stats.collection.page.PageFishing;
import joshie.harvest.core.gui.stats.collection.page.PageShipping;
import joshie.harvest.core.gui.stats.relations.page.PageAnimals;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GuiStats extends GuiBaseBook {
    private static final ResourceLocation LEFT_GUI = new ResourceLocation(MODID, "textures/gui/book_cooking_left.png");
    private static final ResourceLocation RIGHT_GUI = new ResourceLocation(MODID, "textures/gui/book_cooking_right.png");
    private static final int imageWidth = 154;
    private static final int imageHeight = 202;

    public GuiStats() {
        super(LEFT_GUI, RIGHT_GUI, imageWidth, imageHeight);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new ButtonTabLeft(this, PageShipping.INSTANCE, buttonList.size(), -26, 32));
        buttonList.add(new ButtonTabLeft(this, PageAnimals.INSTANCE, buttonList.size(), -26, 64));
    }

    @Override
    public BookPage getDefaultPage() {
        return PageFishing.INSTANCE;
    }
}

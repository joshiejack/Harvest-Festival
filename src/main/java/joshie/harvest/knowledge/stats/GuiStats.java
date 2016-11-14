package joshie.harvest.knowledge.stats;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.base.gui.GuiBaseBook;
import joshie.harvest.knowledge.stats.button.ButtonTabLeft;
import joshie.harvest.knowledge.stats.collection.page.PageFishing;
import joshie.harvest.knowledge.stats.collection.page.PageShipping;
import joshie.harvest.knowledge.stats.notes.page.PageTown;
import joshie.harvest.knowledge.stats.quests.page.PageQuests;
import joshie.harvest.knowledge.stats.relations.page.PageAnimals;
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
        buttonList.add(new ButtonTabLeft(this, PageAnimals.INSTANCE, buttonList.size(), -26, 66));
        buttonList.add(new ButtonTabLeft(this, PageTown.INSTANCE, buttonList.size(), -26, 100));
        buttonList.add(new ButtonTabLeft(this, PageQuests.INSTANCE, buttonList.size(), -26, 134));
    }

    @Override
    public BookPage getDefaultPage() {
        return PageFishing.INSTANCE;
    }
}

package joshie.harvest.knowledge.gui.stats;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.base.gui.GuiBaseBook;
import joshie.harvest.knowledge.gui.stats.button.ButtonTabLeft;
import joshie.harvest.knowledge.gui.stats.collection.page.PageShipping;
import joshie.harvest.knowledge.gui.stats.notes.page.PageTown;
import joshie.harvest.knowledge.gui.stats.quests.page.PageQuests;
import joshie.harvest.knowledge.gui.stats.relations.page.PageAnimals;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GuiStats extends GuiBaseBook {
    private static final ResourceLocation LEFT_GUI = new ResourceLocation(MODID, "textures/gui/book_cooking_left.png");
    private static final ResourceLocation RIGHT_GUI = new ResourceLocation(MODID, "textures/gui/book_cooking_right.png");
    private static final ItemStack NOTES = new ItemStack(Items.WRITABLE_BOOK);
    private static final int imageWidth = 154;
    private static final int imageHeight = 202;
    public static BookPage collection;
    public static BookPage relationships;
    public static BookPage notes;
    public static BookPage quests;

    public GuiStats() {
        super(LEFT_GUI, RIGHT_GUI, imageWidth, imageHeight);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new ButtonTabLeft(this, PageShipping.INSTANCE, buttonList.size(), -26, 32) {
            @Override
            public BookPage getNewPage() { return collection; }
        });

        buttonList.add(new ButtonTabLeft(this, PageAnimals.INSTANCE, buttonList.size(), -26, 66){
            @Override
            public BookPage getNewPage() { return relationships; }
        });

        buttonList.add(new ButtonTabLeft(this, PageTown.INSTANCE, buttonList.size(), -26, 100) {
            @Override
            public BookPage getNewPage() { return notes; }

            @Override
            public ItemStack getIcon() {
                return NOTES;
            }
        });

        buttonList.add(new ButtonTabLeft(this, PageQuests.INSTANCE, buttonList.size(), -26, 134){
            @Override
            public BookPage getNewPage() { return quests;}
        });
    }

    @Override
    public BookPage getDefaultPage() {
        return PageShipping.INSTANCE;
    }

    @Override
    public void setPage(BookPage newPage) {
        super.setPage(newPage);
    }
}

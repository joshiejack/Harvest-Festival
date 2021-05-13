package uk.joshiejack.husbandry.client.gui;

import uk.joshiejack.husbandry.client.gui.pages.PageStats;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static uk.joshiejack.husbandry.Husbandry.MODID;

@SideOnly(Side.CLIENT)
public class GuiTracker extends GuiBook {
    public static final ResourceLocation LEFT_GUI = new ResourceLocation(MODID, "textures/gui/book_left.png");
    private static final ResourceLocation RIGHT_GUI = new ResourceLocation(MODID, "textures/gui/book_right.png");
    public static final GuiTracker INSTANCE = new GuiTracker();

    public GuiTracker() {
        super(LEFT_GUI, RIGHT_GUI, 154, 202);
    }


    @Override
    protected void initTabs(List<GuiButton> buttonList) {}

    @Override
    public Page getDefaultPage() {
        return PageStats.INSTANCE;
    }
}

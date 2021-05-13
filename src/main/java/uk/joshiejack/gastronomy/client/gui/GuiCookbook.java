package uk.joshiejack.gastronomy.client.gui;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.client.gui.pages.PageApplianceList;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCookbook extends GuiBook {
    public static final ResourceLocation LEFT_GUI = GuiElements.getTexture(Gastronomy.MODID, "book_cooking_left");
    private static final ResourceLocation RIGHT_GUI = GuiElements.getTexture(Gastronomy.MODID, "book_cooking_right");
    public static final PageApplianceList APPLIANCE_LIST = new PageApplianceList();
    public static final GuiCookbook INSTANCE = new GuiCookbook();

    public GuiCookbook() {
        super(LEFT_GUI, RIGHT_GUI, 154, 202);
    }

    @Override
    protected void initTabs(List<GuiButton> buttonList) {}

    @Override
    public Page getDefaultPage() {
        return APPLIANCE_LIST;
    }
}

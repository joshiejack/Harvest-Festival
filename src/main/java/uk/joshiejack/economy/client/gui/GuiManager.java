package uk.joshiejack.economy.client.gui;

import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.client.gui.page.PageEconomyManager;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiManager extends GuiBook {
    public static final ResourceLocation LEFT_GUI = GuiElements.getTexture(Economy.MODID, "book_left");
    private static final ResourceLocation RIGHT_GUI = GuiElements.getTexture(Economy.MODID, "book_right");
    public static final GuiManager INSTANCE = new GuiManager();

    public GuiManager() {
        super(LEFT_GUI, RIGHT_GUI, 154, 202);
        fontColor1 = 4210752;
        fontColor2 = 0x3F3F3F;
    }

    @Override
    protected void initTabs(List<GuiButton> buttonList) {}

    @Override
    public Page getDefaultPage() {
        return PageEconomyManager.INSTANCE;
    }
}

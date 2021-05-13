package uk.joshiejack.settlements.client.gui;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.client.gui.page.PageQuests;
import uk.joshiejack.settlements.client.gui.page.PageRelationships;
import uk.joshiejack.settlements.client.gui.page.PageTownManager;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonTabPage;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;


@SideOnly(Side.CLIENT)
public class GuiJournal extends GuiBook {
    public static final GuiJournal INSTANCE = new GuiJournal();
    public static final ResourceLocation ICONS = GuiElements.getTexture(Settlements.MODID, "icons");
    public final List<UUID> members = Lists.newArrayList();

    public GuiJournal() {
        super(GuiElements.BOOK_LEFT, GuiElements.BOOK_RIGHT, 154, 202);
    }

    public static void setMembers(List<UUID> members) {
        INSTANCE.members.clear();
        INSTANCE.members.addAll(members);
    }

    @Override
    protected void initTabs(List<GuiButton> buttonList) {
        buttonList.add(new ButtonTabPage(this, PageRelationships.INSTANCE, buttonList.size(), -26, 62, false));
        buttonList.add(new ButtonTabPage(this, PageQuests.INSTANCE, buttonList.size(), -26, 96, false)); //All only
        buttonList.add(new ButtonTabPage(this, PageTownManager.INSTANCE, buttonList.size(), -26, 130, false));
    }

    @Override
    public Page getDefaultPage() {
        return PageRelationships.INSTANCE;
    }
}

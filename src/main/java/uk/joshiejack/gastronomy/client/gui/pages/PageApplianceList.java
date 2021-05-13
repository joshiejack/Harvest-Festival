package uk.joshiejack.gastronomy.client.gui.pages;

import com.google.common.collect.Maps;
import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.client.gui.buttons.ButtonApplianceList;
import uk.joshiejack.gastronomy.client.gui.labels.LabelIntroduction;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class PageApplianceList extends AbstractPageCookbook {
    public static Map<Appliance, PageRecipeList> RECIPE_LISTS = Maps.newHashMap();

    public PageApplianceList() {
        super(null);
        for (Appliance appliance: Appliance.values()) {
            RECIPE_LISTS.put(appliance, new PageRecipeList(appliance));
        }
    }

    @Override
    public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(buttonList, labelList);
        labelList.add(new LabelIntroduction(gui));
        int id = 0;
        for (Appliance appliance: Appliance.values()) {
            PageRecipeList list = RECIPE_LISTS.get(appliance);
            if (list.hasRecipes()) {
                buttonList.add(new ButtonApplianceList(appliance, gui, buttonList.size(), 163, 20 + (id * 31), ""));
                id++;
            }
        }
    }
}

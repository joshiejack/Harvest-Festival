package joshie.harvest.debug;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.tools.HFTools;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFCommand
public class CommandExportToolsList extends CommandExportHeld {
    @Override
    public String getCommandName() {
        return "export-tools-list";
    }

    @Override
    public boolean isExportable(ItemStack stack) {
        return true;
    }

    @Override
    protected void export(ItemStack stack, String... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append("<includeonly>{| class=\"wikitable\" id=\"navbox\"\n" +
                "! colspan=2 | [[Tools]]\n");

        builder.append("|-\n" +
                "!" + "Upgradeable" +"\n|");
        List<String> tools = new ArrayList<>();
        for (ToolTier tier: ToolTier.values()) {
            tools.add(HFTools.HOE.getStack(tier).getDisplayName());
        }

        for (ToolTier tier: ToolTier.values()) {
            tools.add(HFTools.WATERING_CAN.getStack(tier).getDisplayName());
        }

        for (ToolTier tier: ToolTier.values()) {
            tools.add(HFTools.SICKLE.getStack(tier).getDisplayName());
        }

        for (ToolTier tier: ToolTier.values()) {
            tools.add(HFTools.AXE.getStack(tier).getDisplayName());
        }

        for (ToolTier tier: ToolTier.values()) {
            tools.add(HFTools.HAMMER.getStack(tier).getDisplayName());
        }

        for (ToolTier tier: ToolTier.values()) {
            tools.add(HFFishing.FISHING_ROD.getStack(tier).getDisplayName());
        }

        addToBuilderAndSort("Upgradeable", tools, builder, false);


        List<String> extra = new ArrayList<>();
        extra.add("Milker");
        extra.add("Shears");
        extra.add("Brush");
        addToBuilderAndSort("Other", extra, builder);

        builder.append("\n|-\n" +
        "|}{{mainonly|[[Category:Tools]]}}</includeonly><noinclude>{{{{FULLPAGENAME}}/doc}}</noinclude>\n");
        Debug.save(builder);
    }

    private void addToBuilderAndSort(String name, List<String> list, StringBuilder builder, boolean sort) {
        if (sort) {
            builder.append("\n|-\n" +
                    "!" + name +"\n|");
            Collections.sort(list, (o1, o2) -> {return o1.compareTo(o2); });
        }

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) builder.append(" • ");
            builder.append("[[" + list.get(i) + "]]");
        }
    }

    private void addToBuilderAndSort(String name, List<String> list, StringBuilder builder) {
        addToBuilderAndSort(name, list, builder, true);
    }
}

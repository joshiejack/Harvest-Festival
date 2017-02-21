package joshie.harvest.debug;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.tools.HFTools;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFDebugCommand
@SuppressWarnings("unused")
public class CommandExportMiningList extends CommandExportHeld {
    @Override
    public String getCommandName() {
        return "export-mineral-list";
    }

    @Override
    public boolean isExportable(ItemStack stack) {
        return true;
    }

    @Override
    protected void export(ItemStack stack, String... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append("<includeonly>{| class=\"wikitable\" id=\"navbox\"\n" +
                "! colspan=2 | [[Mining]]");

        List<String> tools = new ArrayList<>();
        tools.add(new ItemStack(HFMining.LADDER).getDisplayName());
        tools.add(new ItemStack(HFMining.MINING_TOOL).getDisplayName());
        addToBuilderAndSort("Equipment", tools, builder);

        builder.append(" • ");
        List<String> rods = new ArrayList<>();
        for (ToolTier tier: ToolTier.values()) {
            rods.add(HFTools.HAMMER.getStack(tier).getDisplayName());
        }

        addToBuilderAndSort("Equipment", rods, builder, false);

        List<String> oreList = new ArrayList<>();
        for (Material material: Material.values()) {
            if (material.isOre()) {
                oreList.add(TextFormatting.getTextWithoutFormattingCodes(HFMining.MATERIALS.getStackFromEnum(material).getDisplayName()));
            }
        }

        addToBuilderAndSort("Ores", oreList, builder);

        List<String> gemList = new ArrayList<>();
        for (Material material: Material.values()) {
            if (!material.isOre()) {
                gemList.add(TextFormatting.getTextWithoutFormattingCodes(HFMining.MATERIALS.getStackFromEnum(material).getDisplayName()));
            }
        }

        gemList.add("Diamond");
        gemList.add("Emerald");
        addToBuilderAndSort("Gems", gemList, builder);

        builder.append("\n|-\n" +
        "|}{{mainonly|[[Category:Mining]]}}</includeonly><noinclude>{{{{FULLPAGENAME}}/doc}}</noinclude>\n");
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

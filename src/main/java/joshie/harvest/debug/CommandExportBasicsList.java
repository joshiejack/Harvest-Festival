package joshie.harvest.debug;

import joshie.harvest.core.commands.HFDebugCommand;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFDebugCommand
@SuppressWarnings("unused")
public class CommandExportBasicsList extends CommandExportHeld {
    @Override
    public String getCommandName() {
        return "export-basic-list";
    }

    @Override
    public boolean isExportable(ItemStack stack) {
        return true;
    }

    @Override
    protected void export(ItemStack stack, String... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append("<includeonly>{| class=\"wikitable\" id=\"navbox\"\n" +
                "! colspan=2 | [[Basics]]");

        List<String> basics = new ArrayList<>();
        basics.add("Shipment Box");
        basics.add("Goddess Flower");
        basics.add("Book of Statistics");
        addToBuilderAndSort("Basics", basics, builder);
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

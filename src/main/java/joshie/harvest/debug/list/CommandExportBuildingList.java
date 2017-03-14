package joshie.harvest.debug.list;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.debug.CommandExportHeld;
import joshie.harvest.debug.Debug;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@HFDebugCommand
@SuppressWarnings("unused")
public class CommandExportBuildingList extends CommandExportHeld {
    @Override
    public String getCommandName() {
        return "export-building-list";
    }

    @Override
    public boolean isExportable(ItemStack stack) {
        return true;
    }


    private static final String CAT1 = "Town";
    private static final String CAT2 = "Buildings";

    @Override
    protected void export(ItemStack stack, String... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append("<includeonly>{| class=\"wikitable\" id=\"navbox\"\n" +
                "! colspan=2 | [[" + CAT1 + "]]\n" +
                "|-\n" +
                "!" + CAT2 + "\n|");

        List<String> list = Building.REGISTRY.values().stream().map(Building::getLocalisedName).collect(Collectors.toList());
        Collections.sort(list, String::compareTo);

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) builder.append(" • ");
            builder.append("[[").append(list.get(i)).append("]]");
        }

        builder.append("\n|-\n" +
        "|}{{mainonly|[[Category:" + CAT2+ "]][[Category:" + CAT1 + "]]}}</includeonly><noinclude>{{{{FULLPAGENAME}}/doc}}</noinclude>\n");
        Debug.save(builder);
    }
}

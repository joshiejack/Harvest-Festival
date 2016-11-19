package joshie.harvest.debug;

import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFCommand
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

        List<String> list = new ArrayList<>();
        for (BuildingImpl building: BuildingRegistry.REGISTRY) {
            if (building != HFBuildings.null_building) {
                list.add(building.getLocalisedName());
            }
        }

        Collections.sort(list, (o1, o2) -> {return o1.compareTo(o2); });

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) builder.append(" • ");
            builder.append("[[" + list.get(i) + "]]");
        }

        builder.append("\n|-\n" +
        "|}{{mainonly|[[Category:" + CAT2+ "]][[Category:" + CAT1 + "]]}}</includeonly><noinclude>{{{{FULLPAGENAME}}/doc}}</noinclude>\n");
        Debug.save(builder);
    }
}

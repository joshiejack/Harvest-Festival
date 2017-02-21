package joshie.harvest.debug;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood.FishType;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFDebugCommand
@SuppressWarnings("unused")
public class CommandExportFishList extends CommandExportHeld {
    @Override
    public String getCommandName() {
        return "export-fish-list";
    }

    @Override
    public boolean isExportable(ItemStack stack) {
        return true;
    }

    @Override
    protected void export(ItemStack stack, String... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append("<includeonly>{| class=\"wikitable\" id=\"navbox\"\n" +
                "! colspan=2 | [[Fishing]]");

        List<String> tools = new ArrayList<>();
        tools.add("Bait");
        tools.add("Hatchery");
        tools.add("Fish Trap");
        addToBuilderAndSort("Equipment", tools, builder);
        builder.append(" • ");

        List<String> rods = new ArrayList<>();
        for (ToolTier tier: ToolTier.values()) {
            rods.add(HFFishing.FISHING_ROD.getStack(tier).getDisplayName());
        }

        addToBuilderAndSort("Equipment", rods, builder, false);

        List<String> fishList = new ArrayList<>();
        for (Fish fish: Fish.values()) {
            fishList.add(HFFishing.FISH.getStackFromEnum(fish).getDisplayName());
        }

        for (FishType type: FishType.values()) {
            fishList.add(new ItemStack(Items.FISH, 1, type.ordinal()).getDisplayName());
        }

        addToBuilderAndSort("Fish", fishList, builder);

        List<String> junkList = new ArrayList<>();
        for (Junk junk: Junk.values()) {
            if (junk == Junk.BAIT) continue;
            junkList.add(HFFishing.JUNK.getStackFromEnum(junk).getDisplayName());
        }

        junkList.add("Small Branch");
        addToBuilderAndSort("Loot", junkList, builder);

        builder.append("\n|-\n" +
        "|}{{mainonly|[[Category:Fishing]]}}</includeonly><noinclude>{{{{FULLPAGENAME}}/doc}}</noinclude>\n");
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

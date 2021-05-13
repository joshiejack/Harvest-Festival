package uk.joshiejack.data.wiki;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.npcs.gifts.GiftCategory;
import uk.joshiejack.settlements.npcs.gifts.GiftQuality;
import uk.joshiejack.settlements.npcs.gifts.GiftRegistry;
import uk.joshiejack.data.Data;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class NPCExport implements Runnable {
    private static final Map<String, String> NAMES = Maps.newHashMap();

    static {
        NAMES.put("cooking", "Cooked Food");
        NAMES.put("money", "Coins");
        NAMES.put("milk", "Milk");
        NAMES.put("wool", "Wool");
        NAMES.put("art", "Art Items");
        NAMES.put("knowledge", "Knowledge Items");
        NAMES.put("magic", "Magic Items");
        NAMES.put("machine", "Mechanical Items");
        NAMES.put("fish", "Fishes");
        NAMES.put("building", "Construction Materials");
        NAMES.put("monster", "Monster Drops");
        NAMES.put("junk", "Junk Items");
    }

    private String getStringForCategory(GiftCategory category) {
        if (NAMES.containsKey(category.name())) return NAMES.get(category.name());
        else return WordUtils.capitalize(category.name() + "s");
    }

    @SuppressWarnings("unchecked")
    public StringBuilder build(NPC npc) {
        List<GiftQuality> qualities = Lists.newArrayList(((Map<String, GiftQuality>) ReflectionHelper.getPrivateValue(GiftQuality.class, null, "REGISTRY")).values());
        final Collection<Pair<Holder, GiftQuality>> stackRegistry = GiftRegistry.ITEM_OVERRIDES.get(npc);
        final Map<GiftCategory, GiftQuality> categoryRegistry = GiftRegistry.CATEGORY_OVERRIDES.get(npc);
        HashMultimap<GiftQuality, GiftCategory> qualityToCategories = HashMultimap.create();

        for (Map.Entry<GiftCategory, GiftQuality> entry : categoryRegistry.entrySet()) {
            qualityToCategories.get(entry.getValue()).add(entry.getKey());
        }

        for (GiftCategory c : GiftCategory.REGISTRY.values()) {
            if (!categoryRegistry.containsKey(c) && !c.name().equals("none")) {
                qualityToCategories.get(c.quality()).add(c);
            }
        }

        //Build everything

        Set<String> handled = new HashSet<>();
        List<ItemStack> exceptions = new ArrayList<>();
        for (Pair<Holder, GiftQuality> pair : stackRegistry) {
            exceptions.addAll(pair.getLeft().getStacks());
        }

        qualities.sort((q1, q2) -> Integer.compare(q2.value(), q1.value()));

        StringBuilder builder2 = new StringBuilder();
        ;
        for (GiftQuality quality : qualities) {
            StringBuilder builder = new StringBuilder();
            builder.append("===").append(WordUtils.capitalize(quality.name())).append("===");
            builder.append("\n{{quote|");
            builder.append(StringHelper.localize(npc.getUnlocalizedKey() + ".gift." + quality.name()));
            builder.append("}} \n" +
                    "{| class=\"wikitable\" id=\"roundedborder\" style=\"width:50%; min-width:100px;\"");
            builder.append("\n!style=\"width: 48px;\"|Image\n" +
                    "!Name");
            builder.append("\n");

            if (qualityToCategories.get(quality).size() > 0) {
                builder.append("|-\n|[[File:" + npc.getLocalizedName() + ".png|48px|center]]");
                builder.append("\n|<ul>");
            }

            for (GiftCategory category : qualityToCategories.get(quality)) {
                StringBuilder eString = new StringBuilder();
                Set<String> added = new HashSet<>();
                boolean initial = false;
                for (ItemStack stack : exceptions) {
                    String name = Export.getEntryName(stack);
                    GiftCategory sCategory = GiftRegistry.CATEGORY_REGISTRY.getValue(stack);
                    if (!sCategory.name().equals("none") && sCategory.name().equals(category.name()) && !added.contains(name)) {
                        if (!initial) {
                            eString.append("[[" + name + "]]");
                            initial = true;
                        } else {
                            eString.append(", ");
                            eString.append("[[" + name + "]]");
                        }

                        added.add(name);
                    }
                }


                builder.append("<li>'''All [[");
                builder.append(getStringForCategory(category));
                builder.append("]]''' ");

                if (initial) {
                    builder.append("''(except ");
                    builder.append(eString);
                    builder.append(")''");
                }

                builder.append("</li>\n");
            }

            if (qualityToCategories.get(quality).size() > 0) builder.append("</ul>\n");
            for (Pair<Holder, GiftQuality> pair : stackRegistry) {
                if (quality.name().equals(pair.getRight().name())) {
                    for (ItemStack stack : pair.getLeft().getStacks()) {
                        if (!handled.contains(Export.getEntryName(stack))) {
                            builder.append("|-\n" + "|[[File:").append(Export.getEntryName(stack)).append(".png|48px|center]]\n").append("|[[").append(Export.getEntryName(stack)).append("]]\n");
                            handled.add(Export.getEntryName(stack));
                        }
                    }
                }
            }

            builder.append("|}\n\n");
            builder2.append(builder);
        }

        return builder2;
    }

    @Override
    public void run() {
        for (NPC npc : NPC.all()) {
            Data.output("npc_" + npc.getRegistryName().getPath(), build(npc));
        }
    }
}

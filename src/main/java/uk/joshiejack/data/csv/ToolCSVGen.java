package uk.joshiejack.data.csv;

import uk.joshiejack.data.Data;
import org.apache.commons.lang3.text.WordUtils;

public class ToolCSVGen implements Runnable {
    private void listings(String name, StringBuilder output) {
        output.append("#" + WordUtils.capitalize(name) + "\n" +
                "blacksmith_upgrades," + name + "_axe,limited_1,default\n" +
                "blacksmith_upgrades," + name + "_hammer,limited_1,default\n" +
                "blacksmith_upgrades," + name + "_shovel,limited_1,default\n" +
                "blacksmith_upgrades," + name + "_hoe,limited_1,default\n" +
                "blacksmith_upgrades," + name + "_watering_can,limited_1,default\n" +
                "blacksmith_upgrades," + name + "_sickle,limited_1,default\n" +
                "blacksmith_upgrades," + name + "_fishing_rod,limited_1,default\n" +
                "blacksmith_upgrades," + name + "_sword,limited_1,default\n");
    }

    private void conditions(String upgrade_to, String upgrade_from, StringBuilder output) {
        output.append("#" + WordUtils.capitalize(upgrade_to) + "\n" +
                "blacksmith_upgrades," + upgrade_to + "_axe,has_" + upgrade_from + "_axe\n" +
                "blacksmith_upgrades," + upgrade_to + "_hammer,has_" + upgrade_from + "_hammer\n" +
                "blacksmith_upgrades," + upgrade_to + "_shovel,has_" + upgrade_from + "_shovel\n" +
                "blacksmith_upgrades," + upgrade_to + "_hoe,has_" + upgrade_from + "_hoe\n" +
                "blacksmith_upgrades," + upgrade_to + "_watering_can,has_" + upgrade_from + "_watering_can\n" +
                "blacksmith_upgrades," + upgrade_to + "_sickle,has_" + upgrade_from + "_sickle\n" +
                "blacksmith_upgrades," + upgrade_to + "_fishing_rod,has_" + upgrade_from + "_fishing_rod\n" +
                "blacksmith_upgrades," + upgrade_to + "_sword,has_" + upgrade_from + "_sword\n");
    }

    private void display(String name, StringBuilder output) {
        output.append("#" + WordUtils.capitalize(name) + "\n" +
                "blacksmith_upgrades," + name + "_axe,default,harvestfestival:" + name + "_axe,,\n" +
                "blacksmith_upgrades," + name + "_hammer,default,harvestfestival:" + name + "_hammer,,\n" +
                "blacksmith_upgrades," + name + "_shovel,default,harvestfestival:" + name + "_shovel,,\n" +
                "blacksmith_upgrades," + name + "_hoe,default,harvestfestival:" + name + "_hoe,,\n" +
                "blacksmith_upgrades," + name + "_watering_can,default,harvestfestival:" + name + "_watering_can,,\n" +
                "blacksmith_upgrades," + name + "_sickle,default,harvestfestival:" + name + "_sickle,,\n" +
                "blacksmith_upgrades," + name + "_fishing_rod,default,harvestfestival:" + name + "_fishing_rod,,\n" +
                "blacksmith_upgrades," + name + "_sword,default,harvestfestival:" + name + "_sword,,\n");
    }

    private void materials(String upgrade_from, String upgrade_to, String material, StringBuilder output) {
        output.append("#" + WordUtils.capitalize(upgrade_to) + "\n" +
                "blacksmith_upgrades," + upgrade_to + "_axe,default,harvestfestival:" + upgrade_from + "_axe,1\n" +
                "blacksmith_upgrades," + upgrade_to + "_axe,default," + material + ",5\n" +
                "blacksmith_upgrades," + upgrade_to + "_hammer,default,harvestfestival:" + upgrade_from + "_hammer,1\n" +
                "blacksmith_upgrades," + upgrade_to + "_hammer,default," + material + ",5\n" +
                "blacksmith_upgrades," + upgrade_to + "_shovel,default,harvestfestival:" + upgrade_from + "_shovel,1\n" +
                "blacksmith_upgrades," + upgrade_to + "_shovel,default," + material + ",5\n" +
                "blacksmith_upgrades," + upgrade_to + "_hoe,default,harvestfestival:" + upgrade_from + "_hoe,1\n" +
                "blacksmith_upgrades," + upgrade_to + "_hoe,default," + material + ",5\n" +
                "blacksmith_upgrades," + upgrade_to + "_watering_can,default,harvestfestival:" + upgrade_from + "_watering_can,1\n" +
                "blacksmith_upgrades," + upgrade_to + "_watering_can,default," + material + ",5\n" +
                "blacksmith_upgrades," + upgrade_to + "_sickle,default,harvestfestival:" + upgrade_from + "_sickle,1\n" +
                "blacksmith_upgrades," + upgrade_to + "_sickle,default," + material + ",5\n" +
                "blacksmith_upgrades," + upgrade_to + "_fishing_rod,default,harvestfestival:" + upgrade_from + "_fishing_rod,1\n" +
                "blacksmith_upgrades," + upgrade_to + "_fishing_rod,default," + material + ",5\n" +
                "blacksmith_upgrades," + upgrade_to + "_sword,default,harvestfestival:" + upgrade_from + "_sword,1\n" +
                "blacksmith_upgrades," + upgrade_to + "_sword,default," + material + ",5\n");
    }

    private void condition(String name, StringBuilder output) {
        output.append("#" + WordUtils.capitalize(name) + "\n" +
                "has_" + name + "_axe,compare\n" +
                "has_" + name + "_hammer,compare\n" +
                "has_" + name + "_shovel,compare\n" +
                "has_" + name + "_hoe,compare\n" +
                "has_" + name + "_watering_can,compare\n" +
                "has_" + name + "_sickle,compare\n" +
                "has_" + name + "_fishing_rod,compare\n" +
                "has_" + name + "_sword,compare\n");
    }

    private void compare(String name, StringBuilder output) {
        output.append("#" + WordUtils.capitalize(name) + "\n" +
                "has_" + name + "_axe,true," + name + "_axe_count,false,true,true,one\n" +
                "has_" + name + "_hammer,true," + name + "_hammer_count,false,true,true,one\n" +
                "has_" + name + "_shovel,true," + name + "_shovel_count,false,true,true,one\n" +
                "has_" + name + "_hoe,true," + name + "_hoe_count,false,true,true,one\n" +
                "has_" + name + "_watering_can,true," + name + "_watering_can_count,false,true,true,one\n" +
                "has_" + name + "_sickle,true," + name + "_sickle_count,false,true,true,one\n" +
                "has_" + name + "_fishing_rod,true," + name + "_fishing_rod_count,false,true,true,one\n" +
                "has_" + name + "_sword,true," + name + "_sword_count,false,true,true,one\n");;
    }

    private void comparators(String name, StringBuilder output) {
        output.append("#" + WordUtils.capitalize(name) + "\n" +
                "" + name + "_axe_count,in_inventory\n" +
                "" + name + "_hammer_count,in_inventory\n" +
                "" + name + "_shovel_count,in_inventory\n" +
                "" + name + "_hoe_count,in_inventory\n" +
                "" + name + "_watering_can_count,in_inventory\n" +
                "" + name + "_sickle_count,in_inventory\n" +
                "" + name + "_fishing_rod_count,in_inventory\n" +
                "" + name + "_sword_count,in_inventory\n");
    }

    private void compare_item(String name, StringBuilder output) {
        output.append("#" + name + "\n" +
                "" + name + "_axe_count,harvestfestival:" + name + "_axe\n" +
                "" + name + "_hammer_count,harvestfestival:" + name + "_hammer\n" +
                "" + name + "_shovel_count,harvestfestival:" + name + "_shovel\n" +
                "" + name + "_hoe_count,harvestfestival:" + name + "_hoe\n" +
                "" + name + "_watering_can_count,harvestfestival:" + name + "_watering_can\n" +
                "" + name + "_sickle_count,harvestfestival:" + name + "_sickle\n" +
                "" + name + "_fishing_rod_count,harvestfestival:" + name + "_fishing_rod\n" +
                "" + name + "_sword_count,harvestfestival:" + name + "_sword\n");
    }

    @Override
    public void run() {
        String[] upgrade_from = new String[] { "copper", "silver", "gold", "blessed" };
        String[] upgrade_to = new String[] { "silver", "gold", "mystril", "mythic" };
        String[] material = new String[] { "silver_ingot", "gold_ingot", "mystril_ingot", "mythic_stone"};
        StringBuilder department_listings = new StringBuilder();;
        for (String s: upgrade_to) {
            listings(s, department_listings);
        }

        StringBuilder listing_conditions = new StringBuilder();
        for (int i = 0; i < upgrade_to.length; i++) {
            conditions(upgrade_to[i], upgrade_from[i], listing_conditions);
        }

        StringBuilder sublisting_display_data = new StringBuilder();;
        for (String s: upgrade_to) {
            display(s, sublisting_display_data);
        }

        StringBuilder sublisting_materials = new StringBuilder();
        for (int i = 0; i < upgrade_to.length; i++) {
            materials(upgrade_from[i], upgrade_to[i], material[i], sublisting_materials);
        }

        StringBuilder conditions = new StringBuilder();
        StringBuilder condition_data_compare = new StringBuilder();
        StringBuilder comparators = new StringBuilder();
        StringBuilder comparator_data_item = new StringBuilder();
        for (String name: upgrade_from) {
            condition(name, conditions);
            compare(name, condition_data_compare);
            comparators(name, comparators);
            compare_item(name, comparator_data_item);
        }

        Data.output("department_listings.csv", department_listings);
        Data.output("listing_conditions.csv", listing_conditions);
        Data.output("sublisting_display_data.csv", sublisting_display_data);
        Data.output("sublisting_materials.csv", sublisting_materials);
        Data.output("conditions.csv", conditions);
        Data.output("condition_data_compare.csv", condition_data_compare);
        Data.output("comparators.csv", comparators);
        Data.output("comparator_data_item.csv", comparator_data_item);
    }
}

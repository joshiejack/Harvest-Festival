package joshie.harvestmoon.init;

import static joshie.harvestmoon.entities.NPC.Age.ADULT;
import static joshie.harvestmoon.entities.NPC.Age.CHILD;
import static joshie.harvestmoon.entities.NPC.Age.ELDER;
import static joshie.harvestmoon.entities.NPC.Gender.FEMALE;
import static joshie.harvestmoon.entities.NPC.Gender.MALE;

import java.util.Collection;
import java.util.HashMap;

import joshie.harvestmoon.entities.NPC;

public class HMNPCs {
    private static HashMap<String, NPC> npcs = new HashMap();

    public static Collection<NPC> getNPCs() {
        return npcs.values();
    }

    public static NPC get(String string) {
        return npcs.get(string);
    }

    public static NPC register(NPC npc) {
        npcs.put(npc.getUnlocalizedName(), npc);
        return npc;
    }

    public static NPC goddess; //The Goddess
    public static NPC animal_owner; // Owner of the Animal Barn
    public static NPC cafe_owner; // Owner of the Cafe
    public static NPC cafe_granny;// Granny of Café Owner
    public static NPC seed_owner; // Owner of Seed Store
    public static NPC seed_child; // Seed Owners Child
    public static NPC tool_owner; // Blacksmith
    public static NPC priest; //Married to mayor
    public static NPC mayor; //Married to priest
    public static NPC daughter_1; //Daughter of Mayor and Priest
    public static NPC daughter_2; //Daughter of Mayor and Priest
    public static NPC clock_worker; //The clock worker
    public static NPC gs_owner; //Owner of general store
    public static NPC miner; //Works in the mines
    public static NPC fisherman; //Fisherman
    public static NPC milkmaid; //Works in the Barn, Milking Cows
    public static NPC builder; //Builds stuff for the players

    public static void init() {
        goddess = register(new NPC("goddess", FEMALE, ADULT));
        animal_owner = register(new NPC("jim", MALE, ADULT));
        cafe_owner = register(new NPC("liara", FEMALE, ADULT));
        seed_owner = register(new NPC("jade", FEMALE, ADULT));
        tool_owner = register(new NPC("daniel", MALE, ADULT));
        priest = register(new NPC("thomas", MALE, ELDER));
        seed_child = register(new NPC("fenn", MALE, CHILD));
        cafe_granny = register(new NPC("katlin", FEMALE, ELDER));
        mayor = register(new NPC("jamie", FEMALE, ELDER));
        daughter_1 = register(new NPC("cloe", FEMALE, ADULT));
        daughter_2 = register(new NPC("abi", FEMALE, CHILD));
        clock_worker = register(new NPC("tiberius", MALE, ADULT));
        gs_owner = register(new NPC("jenni", FEMALE, ADULT));
        miner = register(new NPC("brandon", MALE, ADULT));
        fisherman = register(new NPC("jacob", MALE, ADULT));
        milkmaid = register(new NPC("candice", FEMALE, ADULT));
        builder = register(new NPC("inap", MALE, ADULT).setIsBuilder());
    }
}

package joshie.harvestmoon.init;

import static joshie.harvestmoon.npc.NPC.Age.ADULT;
import static joshie.harvestmoon.npc.NPC.Age.CHILD;
import static joshie.harvestmoon.npc.NPC.Age.ELDER;
import static joshie.harvestmoon.npc.NPC.Gender.FEMALE;
import static joshie.harvestmoon.npc.NPC.Gender.MALE;

import java.util.Collection;
import java.util.HashMap;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.npc.NPC;
import joshie.harvestmoon.npc.NPCGoddess;

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
    public static NPC poultry; //Poultry Farm Owner

    public static void init() {
        goddess = register(new NPCGoddess("goddess", FEMALE, ADULT));
        animal_owner = register(new NPC("jim", MALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
        cafe_owner = register(new NPC("liara", FEMALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
        seed_owner = register(new NPC("jade", FEMALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
        tool_owner = register(new NPC("daniel", MALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
        priest = register(new NPC("thomas", MALE, ELDER, new CalendarDate(1, Season.SPRING, 1)));
        seed_child = register(new NPC("fenn", MALE, CHILD, new CalendarDate(1, Season.SPRING, 1)));
        cafe_granny = register(new NPC("katlin", FEMALE, ELDER, new CalendarDate(1, Season.SPRING, 1)));
        mayor = register(new NPC("jamie", FEMALE, ELDER, new CalendarDate(1, Season.SPRING, 1)));
        daughter_1 = register(new NPC("cloe", FEMALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
        daughter_2 = register(new NPC("abi", FEMALE, CHILD, new CalendarDate(1, Season.SPRING, 1)));
        clock_worker = register(new NPC("tiberius", MALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
        gs_owner = register(new NPC("jenni", FEMALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
        miner = register(new NPC("brandon", MALE, ADULT, new CalendarDate(1, Season.SPRING, 1)).setIsMiner());
        fisherman = register(new NPC("jacob", MALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
        milkmaid = register(new NPC("candice", FEMALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
        builder = register(new NPC("inap", MALE, ADULT, new CalendarDate(1, Season.SPRING, 1)).setIsBuilder());
        poultry = register(new NPC("ondra", MALE, ADULT, new CalendarDate(1, Season.SPRING, 1)));
    }
}

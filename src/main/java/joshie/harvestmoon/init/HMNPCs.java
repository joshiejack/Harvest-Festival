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
import joshie.harvestmoon.player.Town;

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

    public static NPC goddess; //The Goddess                        (SPAWN)
    public static NPC animal_owner; // Owner of the Animal Barn     (BARN)
    public static NPC cafe_owner; // Owner of the Cafe              (CAFE)
    public static NPC cafe_granny;// Granny of Café Owner           (CAFE)
    public static NPC seed_owner; // Sister of Yulif                (CARPENTER)
    public static NPC tool_owner; // Blacksmith                     (BLACKSMITH)
    public static NPC priest; //Married to mayor                    (CHURCH)
    public static NPC mayor; //Married to priest                    (TOWNHALL)
    public static NPC daughter_1; //Daughter of Mayor and Priest    (TOWNHALL)
    public static NPC daughter_2; //Daughter of Mayor and Priest    (TOWNHALL)
    public static NPC clock_worker; //The clock worker              (CLOCKWORKERS)
    public static NPC clockmaker_child; // Clockmakers Child        (CLOCKWORKERS)
    public static NPC gs_owner; //Owner of general store            (GENERAL STORE)
    public static NPC miner; //Works in the mines                   (MINING HUT)
    public static NPC fisherman; //Fisherman                        (FISHING HUT)
    public static NPC milkmaid; //Works in the Barn, Milking Cows   (GENERAL STORE)
    public static NPC builder; //Builds stuff for the players       (SPAWN)
    public static NPC poultry; //Poultry Farm Owner                 (POULTRY FARM)

    public static void preInit() {
        goddess = register(new NPCGoddess("goddess", FEMALE, ADULT).setHeight(1.2F, 0.35F));
        animal_owner = register(new NPC("jim", MALE, ADULT, new CalendarDate(26, Season.SPRING, 1)));
        cafe_owner = register(new NPC("liara", FEMALE, ADULT, new CalendarDate(17, Season.SPRING, 1)));
        seed_owner = register(new NPC("jade", FEMALE, ADULT, new CalendarDate(14, Season.SPRING, 1)));
        daughter_1 = register(new NPC("cloe", FEMALE, ADULT, new CalendarDate(3, Season.SPRING, 1)));
        priest = register(new NPC("thomas", MALE, ELDER, new CalendarDate(9, Season.SUMMER, 1)));
        clockmaker_child = register(new NPC("fenn", MALE, CHILD, new CalendarDate(25, Season.SUMMER, 1)));
        cafe_granny = register(new NPC("katlin", FEMALE, ELDER, new CalendarDate(12, Season.SUMMER, 1)));
        mayor = register(new NPC("jamie", FEMALE, ELDER, new CalendarDate(8, Season.SUMMER, 1)));
        builder = register(new NPC("inap", MALE, ADULT, new CalendarDate(19, Season.SUMMER, 1)).setIsBuilder());
        tool_owner = register(new NPC("daniel", MALE, ADULT, new CalendarDate(1, Season.WINTER, 1)));
        daughter_2 = register(new NPC("abi", FEMALE, CHILD, new CalendarDate(27, Season.WINTER, 1)));
        clock_worker = register(new NPC("tiberius", MALE, ADULT, new CalendarDate(15, Season.WINTER, 1)));
        gs_owner = register(new NPC("jenni", FEMALE, ADULT, new CalendarDate(7, Season.WINTER, 1)));
        miner = register(new NPC("brandon", MALE, ADULT, new CalendarDate(13, Season.AUTUMN, 1)).setIsMiner());
        fisherman = register(new NPC("jacob", MALE, ADULT, new CalendarDate(28, Season.AUTUMN, 1)));
        milkmaid = register(new NPC("candice", FEMALE, ADULT, new CalendarDate(5, Season.AUTUMN, 1)));
        poultry = register(new NPC("ondra", MALE, ADULT, new CalendarDate(16, Season.AUTUMN, 1)));
    }

    public static void init() {
        milkmaid.setHome(HMBuildings.supermarket, Town.SUPERMARKET_BEDROOM);
    }
}

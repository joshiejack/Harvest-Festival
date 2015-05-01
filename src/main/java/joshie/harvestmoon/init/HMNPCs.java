package joshie.harvestmoon.init;

import static joshie.harvestmoon.npc.NPC.Age.ADULT;
import static joshie.harvestmoon.npc.NPC.Age.CHILD;
import static joshie.harvestmoon.npc.NPC.Age.ELDER;
import static joshie.harvestmoon.npc.NPC.Gender.FEMALE;
import static joshie.harvestmoon.npc.NPC.Gender.MALE;

import java.util.Collection;
import java.util.HashMap;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.npc.INPC;
import joshie.harvestmoon.api.npc.INPCRegistry;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.npc.NPC;
import joshie.harvestmoon.npc.NPC.Age;
import joshie.harvestmoon.npc.NPC.Gender;
import joshie.harvestmoon.player.Town;

public class HMNPCs implements INPCRegistry {
    private static HashMap<String, INPC> npcs = new HashMap();

    @Override
    public Collection<INPC> getNPCs() {
        return npcs.values();
    }

    @Override
    public INPC get(String string) {
        return npcs.get(string);
    }

    @Override
    public INPC register(INPC npc) {
        npcs.put(npc.getUnlocalizedName(), npc);
        return npc;
    }

    @Override
    public INPC register(String unlocalised, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth) {
        return register(new NPC(unlocalised, gender, age, new CalendarDate(dayOfBirth, seasonOfBirth, 1)));
    }

    public static INPC goddess; //The Goddess                        (SPAWN) - done
    public static INPC animal_owner; // Owner of the Animal Barn     (BARN)
    public static INPC cafe_owner; // Owner of the Cafe              (CAFE) - done
    public static INPC cafe_granny;// Granny of Café Owner           (CAFE) - done
    public static INPC seed_owner; // Sister of Yulif                (CARPENTER) - done
    public static INPC tool_owner; // Blacksmith                     (BLACKSMITH) - done
    public static INPC priest; //Married to mayor                    (CHURCH) - done
    public static INPC mayor; //Married to priest                    (TOWNHALL) - done
    public static INPC daughter_1; //Daughter of Mayor and Priest    (TOWNHALL) - done
    public static INPC daughter_2; //Daughter of Mayor and Priest    (TOWNHALL) - done
    public static INPC clock_worker; //The clock worker              (CLOCKWORKERS) - done
    public static INPC clockmaker_child; // Clockmakers Child        (CLOCKWORKERS) - done
    public static INPC gs_owner; //Owner of general store            (GENERAL STORE) - done
    public static INPC miner; //Works in the mines                   (MINING HUT) - done
    public static INPC fisherman; //Fisherman                        (FISHING HUT) - done
    public static INPC milkmaid; //Works in the Barn, Milking Cows   (GENERAL STORE) - done
    public static INPC builder; //Builds stuff for the players       (SPAWN) - done
    public static INPC poultry; //Poultry Farm Owner                 (POULTRY FARM) - done

    public static void preInit() {
        goddess = HMApi.NPC.register("goddess", FEMALE, ADULT, 8, Season.SPRING).setHeight(1.2F, 0.05F);
        animal_owner = HMApi.NPC.register("jim", MALE, ADULT, 26, Season.SPRING);
        cafe_owner = HMApi.NPC.register("liara", FEMALE, ADULT, 17, Season.SPRING);
        seed_owner = HMApi.NPC.register("jade", FEMALE, ADULT, 14, Season.SPRING);
        daughter_1 = HMApi.NPC.register("cloe", FEMALE, ADULT, 3, Season.SPRING);
        priest = HMApi.NPC.register("thomas", MALE, ELDER,  9, Season.SUMMER);
        clockmaker_child = HMApi.NPC.register("fenn", MALE, CHILD,  25, Season.SUMMER);
        cafe_granny = HMApi.NPC.register("katlin", FEMALE, ELDER,  12, Season.SUMMER);
        mayor = HMApi.NPC.register("jamie", FEMALE, ELDER,  8, Season.SUMMER);
        builder = HMApi.NPC.register("inap", MALE, ADULT,  19, Season.SUMMER).setIsBuilder();
        tool_owner = HMApi.NPC.register("daniel", MALE, ADULT,  1, Season.WINTER);
        daughter_2 = HMApi.NPC.register("abi", FEMALE, CHILD,  27, Season.WINTER);
        clock_worker = HMApi.NPC.register("tiberius", MALE, ADULT,  15, Season.WINTER);
        gs_owner = HMApi.NPC.register("jenni", FEMALE, ADULT,  7, Season.WINTER);
        miner = HMApi.NPC.register("brandon", MALE, ADULT,  13, Season.AUTUMN).setIsMiner();
        fisherman = HMApi.NPC.register("jacob", MALE, ADULT,  28, Season.AUTUMN);
        milkmaid = HMApi.NPC.register("candice", FEMALE, ADULT,  5, Season.AUTUMN);
        poultry = HMApi.NPC.register("ondra", MALE, ADULT,  16, Season.AUTUMN);
    }

    public static void init() {
        goddess.setHome(HMBuildings.goddessPond, Town.GODDESS);
        animal_owner.setHome(HMBuildings.barn, Town.JIM);
        cafe_owner.setHome(HMBuildings.cafe, Town.LIARA);
        seed_owner.setHome(HMBuildings.carpenter, Town.JADE);
        daughter_1.setHome(HMBuildings.townhall, Town.CLOE);
        priest.setHome(HMBuildings.townhall, Town.TOWNHALL_ADULT_BEDROOM);
        clockmaker_child.setHome(HMBuildings.clockmaker, Town.FENN);
        cafe_granny.setHome(HMBuildings.cafe, Town.KATLIN);
        mayor.setHome(HMBuildings.townhall, Town.JAMIE);
        builder.setHome(HMBuildings.carpenter, Town.CARPENTER_DOWNSTAIRS);
        tool_owner.setHome(HMBuildings.blacksmith, Town.DANIEL);
        daughter_2.setHome(HMBuildings.townhall, Town.ABI);
        clock_worker.setHome(HMBuildings.clockmaker, Town.TIBERIUS);
        gs_owner.setHome(HMBuildings.supermarket, Town.JENNI);
        miner.setHome(HMBuildings.miningHut, Town.BRANDON);
        fisherman.setHome(HMBuildings.fishingHut, Town.JACOB);
        milkmaid.setHome(HMBuildings.supermarket, Town.CANDICE);
        poultry.setHome(HMBuildings.poultryFarm, Town.ONDRA);
    }
}

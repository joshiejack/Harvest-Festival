package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.knowledge.letter.LetterFestival;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static joshie.harvest.api.calendar.Season.SPRING;
import static joshie.harvest.api.calendar.Season.WINTER;
import static joshie.harvest.api.knowledge.Category.TOWNSHIP;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFBUILDING;
import static joshie.harvest.knowledge.HFNotes.registerNote;

@HFLoader(priority = HFBUILDING)
@SuppressWarnings("unchecked")
public class HFFestivals {
    //TODO: Re-enable all the other quests
    private static Map<Festival, Season> TEMP_REGISTRY = new HashMap<>();
    public static final Festival NEW_YEARS = registerFestival("new_years", 1, SPRING).setLength(1);
    public static final Festival COOKING_CONTEST = registerFestival("cooking", 22, SPRING);
    //public static final Festival CHICKEN_FESTIVAL = registerFestival("chicken", 7, SUMMER);
    //public static final Festival COW_FESTIVAL = registerFestival("cow", 20, SUMMER);
    //public static final Festival HARVEST_FESTIVAL = registerFestival("harvest", 9, AUTUMN);
    //public static final Festival SHEEP_FESTIVAL = registerFestival("sheep", 21, AUTUMN);
    //public static final Festival STARRY_NIGHT = registerFestival("starry_night", 24, WINTER);
    public static final Festival NEW_YEARS_EVE = registerFestival("new_years_eve", 30, WINTER);


    public static void init() {
        for (Festival festival: TEMP_REGISTRY.keySet()) {
            String name = "festival." + festival.getResource().getResourcePath().replace("_", ".");
            festival.setQuest(QuestHelper.getQuest(name)).setNote(registerNote(TOWNSHIP, name)).setLetter(new LetterFestival(festival, TEMP_REGISTRY.get(festival), festival.getResource()));
        }

        TEMP_REGISTRY = null; //save memory
    }

    private static Festival registerFestival(String name, int day, Season season) {
        ResourceLocation resource = new ResourceLocation(MODID, name);
        Festival festival = new Festival(resource);
        HFApi.calendar.registerFestival(festival, day, season);
        TEMP_REGISTRY.put(festival, season);
        return festival;
    }
}

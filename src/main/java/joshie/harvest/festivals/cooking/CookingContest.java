package joshie.harvest.festivals.cooking;

import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.festivals.HFFestivals;
import joshie.harvest.quests.QuestHelper;

import static joshie.harvest.api.calendar.Season.SPRING;
import static joshie.harvest.api.knowledge.Category.TOWNSHIP;
import static joshie.harvest.core.lib.LoadOrder.HFBUILDING;
import static joshie.harvest.knowledge.HFNotes.registerNote;

@HFLoader(priority = HFBUILDING)
public class CookingContest {
    public static final Festival FESTIVAL = HFFestivals.registerFestival("cooking", 22, SPRING);
    public static final Script SCRIPT = new CookingContestScript();

    public static void init() {
        FESTIVAL.setQuest(QuestHelper.getQuest("festival.cooking")).setNote(registerNote(TOWNSHIP, "festival.cooking")).setLetter(new CookingContestLetter());
    }
}

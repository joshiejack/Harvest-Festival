package uk.joshiejack.settlements.quest.settings;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Map.Entry;

public class QuestBoardClient {
    private static final Map<ResourceLocation, QuestInfo> dailies = Maps.newHashMap();
    private static Entry<ResourceLocation, QuestInfo> next;

    public static boolean hasDailyQuest() {
        return next != null;
    }

    public static void setDailies(Map<ResourceLocation, Pair<String, String>> dailyMap) {
        dailies.clear();
        dailyMap.forEach((k, v) -> dailies.put(k, new QuestInfo(v.getLeft(), v.getRight())));
        if (dailies.size() > 0) {
            next = QuestBoardClient.dailies.entrySet().iterator().next();
        }
    }

    public static Entry<ResourceLocation, QuestInfo> getNextQuest() {
        return next;
    }

    public static class QuestInfo {
        private final ITextComponent[] title;
        private final String description;

        private QuestInfo(String title, String description) {
            this.title = new ITextComponent[]{
                    new TextComponentTranslation(title),
                    new TextComponentString("REQUIRED!"),
                    new TextComponentString(""),
                    new TextComponentString("Read for info")};;
            this.description = description;
        }

        public QuestInfo of(String title, String description) {
            return new QuestInfo(title, description);
        }

        public String getDescription() {
            return description;
        }

        public ITextComponent[] getTitle() {
            return title;
        }
    }
}

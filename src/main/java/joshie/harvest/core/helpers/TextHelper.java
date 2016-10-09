package joshie.harvest.core.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class TextHelper {
    private static final Cache<Triple<String, String, ResourceLocation>, Integer> TRANSLATION_CACHE = CacheBuilder.newBuilder().build();
    private static final Random rand = new Random();

    public static String getSpeech(EntityNPC npc, String text) {
        return getSpeech(npc.getNPC(), text);
    }

    public static String getSpeech(NPC npc, String text) {
        String key = npc.getLocalizationKey() + text;
        if (canTranslate(key)) return localize(key);
        else {
            String generic = npc.getGeneralLocalizationKey() + text;
            return canTranslate(generic) ? localize(generic) : format(MODID + ".npc.error", key, generic);
        }
    }

    public static String getLang() {
        return "en_US";
    }

    public static String getRandomSpeech(INPC npc, final String text, final int maximumAlternatives, Object... data) {
        int maximum = 1;
        try {
            final Triple<String, String, ResourceLocation> key = Triple.of(getLang(), text, npc.getResource());
            maximum = TRANSLATION_CACHE.get(key, () -> {
                int i;
                for (i = 1; i <= maximumAlternatives; i++) {
                    if (!canTranslate(text + i)) break;
                }

                return i - 1;
            });
        } catch (Exception e) {}

        int random = 1 + (maximum >= 2? rand.nextInt(maximum): 0);
        return data.length > 0 ?  format(text + random, data) : localize(text + random);
    }

    @SuppressWarnings("deprecation")
    public static String format(String key, Object... data) {
        return I18n.translateToLocalFormatted(key, data);
    }

    public static String formatHF(String key, Object... data) {
        return I18n.translateToLocalFormatted(MODID + "." + key, data);
    }

    @SuppressWarnings("deprecation")
    public static String localize(String key) {
        return I18n.translateToLocal(key);
    }

    @SuppressWarnings("deprecation")
    public static String localizeFully(String key) {
        return I18n.translateToLocal(key.replace("_", "."));
    }

    @SuppressWarnings("deprecation")
    public static String translate(String s) {
        return I18n.translateToLocal(MODID + "." + s);
    }

    @SuppressWarnings("deprecation")
    public static String translate(String mod, String s) {
        return I18n.translateToLocal(mod + "." + s);
    }

    @SuppressWarnings("deprecation")
    public static boolean canTranslate(String key) {
        return I18n.canTranslate(key);
    }
}
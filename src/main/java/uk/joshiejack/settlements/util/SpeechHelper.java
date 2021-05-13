package uk.joshiejack.settlements.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;

public class SpeechHelper {
    private static final Cache<Pair<String, String>, Integer> TRANSLATION_CACHE = CacheBuilder.newBuilder().build();

    public static String getLang() {
        return "en_US";
    }

    public static String getRandomSpeech(Random rand, final String text, final int maximumAlternatives, Object... data) {
        int maximum = 1;
        try {
            final Pair<String, String> key = Pair.of(getLang(), text);
            maximum = TRANSLATION_CACHE.get(key, () -> {
                int i;
                for (i = 1; i <= maximumAlternatives; i++) {
                    if (!canTranslate(text + i)) break;
                }

                return i - 1;
            });
        } catch (Exception e) {/**/}

        int random = 1 + (maximum >= 2? rand.nextInt(maximum): 0);
        return data.length > 0 ? StringHelper.format(text + random, data) : StringHelper.localize(text + random);
    }

    @SuppressWarnings("deprecation")
    private static boolean canTranslate(String key) {
        return I18n.canTranslate(key);
    }
}

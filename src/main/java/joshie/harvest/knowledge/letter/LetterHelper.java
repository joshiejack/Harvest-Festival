package joshie.harvest.knowledge.letter;

import joshie.harvest.api.core.Letter;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LetterHelper {
    public static boolean hasUnreadLetters(EntityPlayer player) {
        return HFTrackers.getPlayerTrackerFromPlayer(player).getLetters().hasUnreadLetters() ||
                TownHelper.getClosestTownToEntity(player, false).getLetters().hasUnreadLetters();
    }

    public static void addLetterToMailbox(EntityPlayer player, Letter letter) {
        if (!letter.isTownLetter()) HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getLetters().addLetterAndSync(letter);
        else TownHelper.<TownDataServer>getClosestTownToEntity(player, false).getLetters().addLetterAndSync(letter);
        HFTrackers.markTownsDirty();
    }

    @Nonnull
    public static Letter getMostRecentLetter(EntityPlayer player) {
        List<Letter> letterList = new ArrayList<>();
        letterList.addAll(HFTrackers.getPlayerTrackerFromPlayer(player).getLetters().getLetters());
        letterList.addAll(TownHelper.getClosestTownToEntity(player, false).getLetters().getLetters());
        if (letterList.size() == 0) return Letter.NONE;
        letterList.sort((Comparator.comparing(Letter::getPriority)));
        return letterList.get(0);
    }
}

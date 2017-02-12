package joshie.harvest.festivals;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.Letter;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class LetterFestival extends Letter {
    private final Festival festival;
    private final Season season;

    public LetterFestival(Festival festival, Season season, ResourceLocation resource) {
        super(resource);
        this.festival = festival;
        this.season = season;
        this.setRejectable();
        this.setTownLetter();
    }

    @Override
    public boolean isExpired(CalendarDate today, int days) {
        return expires() && days >= getExpiry() || today.getSeason() != season;
    }

    @Override
    public void onLetterAccepted(EntityPlayer player) {
        TownHelper.<TownDataServer>getClosestTownToEntity(player).startFestival(festival);
        onLetterRejected(player); //Remove the letter as well as starting the festival
        HFTrackers.markTownsDirty();
    }

    @Override
    public void onLetterRejected(EntityPlayer player) {
        if (festival.getLetter() != null) { //Just remove the letter
            TownHelper.<TownDataServer>getClosestTownToEntity(player).getLetters().removeLetter(festival.getLetter());
            HFTrackers.markTownsDirty();
        }
    }
}

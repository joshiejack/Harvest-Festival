package joshie.harvest.quests.town.festivals.cow;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.stats.AnimalStatsMilkable;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class CowEntry {
    private final AnimalStatsMilkable stats;
    private EntityPlayer player;
    private NPC npc;
    private int score;

    public CowEntry(@Nullable EntityPlayer thePlayer, @Nonnull EntityHarvestCow theCow) {
        stats = EntityHelper.getStats(theCow);
        if (stats != null) {
            player = thePlayer;
            score += stats.getHappiness(); //Base level
            //Add bonuses, if the animal has had everything done today
            if (stats.performTest(AnimalTest.HAS_EATEN)) score+= 1000;
            if (stats.performTest(AnimalTest.HAD_TREAT)) score += 3000;
            if (stats.performTest(AnimalTest.IS_CLEAN)) score += 2500;
            if (stats.performTest(AnimalTest.BEEN_LOVED)) score += 2000;
            if (stats.performTest(AnimalTest.IS_SICK)) {
                score -= 25000;
            }
        }
    }

    public CowEntry(@Nonnull NPC theNPC, Random rand) {
        stats = null;
        npc = theNPC;
        score = rand.nextInt(20000);
    }

    public int getScore() {
        return score;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public void setWinner() {
        if (stats != null && player != null) {
            player.addExperience(5000);
            stats.performAction(player.worldObj, null, AnimalAction.MAKE_GOLDEN);
        }
    }
}

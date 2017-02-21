package joshie.harvest.quests.town.festivals.cow;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.quests.town.festivals.Place;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;

public class AnimalContestEntry {
    private final AnimalStats stats;
    private EntityPlayer player;
    private NPC npc;
    private int score;

    public AnimalContestEntry(@Nullable EntityPlayer thePlayer, @Nonnull EntityAnimal animal) {
        stats = EntityHelper.getStats(animal);
        if (stats != null) {
            player = thePlayer;
            score += stats.getHappiness(); //Base level
            //Add bonuses, if the animal has had everything done today
            if (stats.performTest(AnimalTest.CAN_CLEAN)) {
                if (stats.performTest(AnimalTest.HAS_EATEN)) score += 1000;
                if (stats.performTest(AnimalTest.HAD_TREAT)) score += 3000;
                if (stats.performTest(AnimalTest.IS_CLEAN)) score += 3000;
                if (stats.performTest(AnimalTest.BEEN_LOVED)) score += 2000;
            } else {
                if (stats.performTest(AnimalTest.HAS_EATEN)) score += 1000;
                if (stats.performTest(AnimalTest.HAD_TREAT)) score += 5000;
                if (stats.performTest(AnimalTest.BEEN_LOVED)) score += 3000;
            }

            //Reduce the score if the animal is sick
            if (stats.performTest(AnimalTest.IS_SICK)) {
                score -= 25000;
            }
        }
    }

    public AnimalContestEntry(@Nullable NPC theNPC, Random rand) {
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

    public void reward(Place place, Set<NPC> npcs, ItemStack[] rewards) {
        if (stats != null && player != null) { //Give the rewards for this
            SpawnItemHelper.addToPlayerInventory(player, rewards[place.ordinal()]);
            stats.affectHappiness(place.happiness); //Make the animal happier, and the npcs that took part v
            for (NPC npc: npcs) HFApi.player.getRelationsForPlayer(player).affectRelationship(npc, place.happiness);
        }
    }
}

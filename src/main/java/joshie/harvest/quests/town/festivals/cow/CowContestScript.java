package joshie.harvest.quests.town.festivals.cow;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npcs.HFNPCs;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class CowContestScript extends Script {
    public CowContestScript() {
        super(new ResourceLocation(MODID, "cow_judge"));
        setNPC(HFNPCs.MILKMAID);
    }

    private String getTextFromScore(int score) {
        if (score <= 0) return "This cow is awful, we need a vet!";
        else if (score <= 3000) return "This cow looks alright.";
        else if (score <= 6000) return "This cow seems decent.";
        else if (score <= 9000) return "This cow is starting to look good.";
        else if (score <= 12000) return "This cow is looking pretty good.";
        else if (score <= 15000) return "This cow is looking good, for sure!.";
        else if (score <= 18000) return "This cow is very good! The owner should be proud";
        else if (score <= 21000) return "This cow doing incredibly well. They sure love their owner.";
        else if (score <= 24000) return "This is doing awesome. Their owner is great.";
        else return "This cow is aboslutely outstanding. Fantastic coat!";
    }

    @Override
    public String getLocalized(EntityAgeable ageable, NPC npc) {
        EntityHarvestCow cow = CowSelection.getClosestCow(ageable.worldObj, new BlockPos(ageable));
        if (cow != null) {
            CowEntry entry = new CowEntry(null, cow);
            return TextHelper.format("Let's take a look at '" + cow.getName() + "'. Well... %s", getTextFromScore(entry.getScore()));
        } else return "Hmm, I guess there's no cow to judge here";
    }
}

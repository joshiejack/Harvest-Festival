package joshie.harvest.quests.selection;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Selection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import javax.annotation.Nullable;

public class FestivalSelection extends Selection {
    public FestivalSelection(String name) {
        super("harvestfestival.quest.festival." + name + ".question", "harvestfestival.quest.festival." + name + ".ready", "harvestfestival.quest.festival." + name + ".cancel");
    }

    @Override
    public Result onSelected(EntityPlayer player, EntityLiving entity, NPC npc, @Nullable Quest quest, int option) {
        if (quest == null) return Result.DENY;

        if (option == 1) {
            quest.increaseStage(player);
            return Result.ALLOW;
        } else return Result.DENY;
    }
}

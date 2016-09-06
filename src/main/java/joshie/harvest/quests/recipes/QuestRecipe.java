package joshie.harvest.quests.recipes;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.core.lib.HFQuests;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class QuestRecipe extends Quest {
    private final String recipe;
    private final int relationship;

    public QuestRecipe(String recipe, INPC npc, int relationship) {
        setNPCs(npc);
        this.recipe = recipe;
        this.relationship = relationship;
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.TUTORIAL_CAFE);
    }

    @Override
    public boolean isRealQuest() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (HFApi.relationships.getRelationship(player, npc) >= relationship) {
            complete(player);
            return "text";
        } else return null;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, CookingHelper.getRecipe(recipe));
    }
}

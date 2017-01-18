package joshie.harvest.quests.town.seeds;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestTown;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Set;

@HFQuest("seeds.progress")
public class QuestProgress extends QuestTown {
    public QuestProgress() {
        setNPCs(HFNPCs.GS_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.BUILDING_CAFE) && finished.contains(Quests.BUILDING_BLACKSMITH)
                && finished.contains(Quests.BUILDING_FISHER) && finished.contains(Quests.BUILDING_FESTIVALS);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        return "complete";
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        complete(player);
    }
}

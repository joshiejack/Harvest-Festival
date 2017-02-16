package joshie.harvest.npcs.npc;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.shops.Shop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class NPCSpecialSeller extends NPC {
    protected Quest quest;
    protected NPC npc;

    @SuppressWarnings("WeakerAccess")
    public NPCSpecialSeller(ResourceLocation resource, Gender gender, Age age, CalendarDate birthday, int insideColor, int outsideColor) {
        super(resource, gender, age, birthday, insideColor, outsideColor);
        this.npc = this;
    }

    @Override //If the current town hasn't enabled selling of sprinklers, then enable it
    public Shop getShop(World world, BlockPos pos, @Nullable EntityPlayer player) {
        if (player != null && quest != null && HFApi.player.getRelationsForPlayer(player).getRelationship(npc) >= 15000) {
            HFApi.quests.completeQuestConditionally(quest, player);
        }

        return super.getShop(world, pos, player);
    }

    public NPCSpecialSeller setQuest(Quest quest) {
        this.quest = quest;
        return this;
    }

    public NPCSpecialSeller setNPC(NPC npc) {
        this.npc = npc;
        return this;
    }
}

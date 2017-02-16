package joshie.harvest.npcs.npc;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class NPCSpecialOpener extends NPCSpecialSeller {
    public NPCSpecialOpener(ResourceLocation resource, Gender gender, Age age, CalendarDate birthday, int insideColor, int outsideColor) {
        super(resource, gender, age, birthday, insideColor, outsideColor);
    }

    @Override //If the current town hasn't enabled selling of sprinklers, then enable it
    public Shop getShop(World world, BlockPos pos, @Nonnull EntityPlayer player) {
        if (quest != null && HFApi.player.getRelationsForPlayer(player).getRelationship(npc) >= 15000) {
            HFApi.quests.completeQuestConditionally(Quests.OPEN_WEDNESDAYS, player);
            HFApi.quests.completeQuestConditionally(quest, player);
        }

        return shop;
    }
}

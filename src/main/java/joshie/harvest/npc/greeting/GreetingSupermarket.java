package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IInfoButton;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.quests.Quests;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GreetingSupermarket implements IInfoButton {
    private final String text;
    private final String text2;

    public GreetingSupermarket(ResourceLocation resourceLocation) {
        this.text = resourceLocation.getResourceDomain() + ".npc." + resourceLocation.getResourcePath() + ".shop";
        this.text2 = resourceLocation.getResourceDomain() + ".npc." + resourceLocation.getResourcePath() + ".shop.wednesday";
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return TownHelper.getClosestTownToEntity(ageable).getQuests().getFinished().contains(Quests.OPEN_WEDNESDAYS) ? TextHelper.localize(text2) : TextHelper.localize(text);
    }
}

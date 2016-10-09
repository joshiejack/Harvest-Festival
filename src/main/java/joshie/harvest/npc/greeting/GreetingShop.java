package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IGreeting;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GreetingShop implements IGreeting {
    private final String text;

    public GreetingShop(ResourceLocation resourceLocation) {
        this.text = resourceLocation.getResourceDomain() + ".npc." + resourceLocation.getResourcePath() + ".shop";
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return TextHelper.localize(text);
    }
}

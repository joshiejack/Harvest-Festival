package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IConditionalGreeting;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GreetingShop implements IConditionalGreeting {
    private final String text;

    public GreetingShop(ResourceLocation resourceLocation) {
        this.text = resourceLocation.getResourceDomain() + ".npc." + resourceLocation.getResourcePath() + ".shop";
    }

    @Override
    public String getUnlocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return text;
    }
}

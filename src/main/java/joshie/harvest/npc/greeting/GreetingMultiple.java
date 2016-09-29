package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IConditionalGreeting;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GreetingMultiple implements IConditionalGreeting {
    private final String text;

    public GreetingMultiple(String text) {
        this.text = MODID + ".npc." + text;
    }

    @Override
    public int getMaximumAlternatives() {
        return 32;
    }

    @Override
    public String getUnlocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return text;
    }
}

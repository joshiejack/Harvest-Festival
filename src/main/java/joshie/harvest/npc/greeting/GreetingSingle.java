package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IConditionalGreeting;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GreetingSingle implements IConditionalGreeting {
    private final String text;

    public GreetingSingle(String text) {
        this.text = MODID + ".npc." + text;
    }

    @Override
    public String getUnlocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return text;
    }
}

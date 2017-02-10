package joshie.harvest.npcs.greeting;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.IConditionalGreeting;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

public abstract class GreetingLocalized implements IConditionalGreeting {
    protected final String text;

    public GreetingLocalized(String text) {
        this.text = text;
    }

    @Override
    public double getDisplayChance() {
        return 5D;
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        return I18n.translateToLocal(text);
    }
}

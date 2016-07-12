package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IConditionalGreeting;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GreetingMultiple implements IConditionalGreeting {
    private String text;

    public GreetingMultiple(String text) {
        this.text = MODID + ".npc." + text;
    }

    @Override
    public int getMaximumAlternatives() {
        return 32;
    }

    @Override
    public String getUnlocalizedText() {
        return text;
    }
}

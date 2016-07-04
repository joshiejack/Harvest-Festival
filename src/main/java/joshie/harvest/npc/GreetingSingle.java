package joshie.harvest.npc;

import joshie.harvest.api.npc.IConditionalGreeting;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GreetingSingle implements IConditionalGreeting {
    public String text;

    public GreetingSingle(String text) {
        this.text = MODID + ".npc." + text;
    }

    @Override
    public String getUnlocalizedText() {
        return text;
    }
}

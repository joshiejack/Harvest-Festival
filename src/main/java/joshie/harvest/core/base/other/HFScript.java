package joshie.harvest.core.base.other;

import joshie.harvest.api.npc.greeting.Script;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class HFScript extends Script {
    public HFScript(String name) {
        super(new ResourceLocation(MODID, name));
    }
}

package joshie.harvest.quests.town.festivals.starry;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ScriptNPCRelated extends Script {
    public ScriptNPCRelated(String name) {
        super(new ResourceLocation(MODID, "starry_" + name));
        unlocalised = "%s.npc.%s..festival.starry.night." + name;
    }

    @Override
    public String getLocalized(NPCEntity entity) {
        return I18n.translateToLocalFormatted(unlocalised, entity.getNPC().getResource().getResourceDomain(), entity.getNPC().getResource().getResourcePath());
    }
}

package joshie.harvest.npc.greeting;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.IConditionalGreeting;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npc.NPCHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GreetingLocation implements IConditionalGreeting {
    private final BuildingLocation location;
    private final String text;

    public GreetingLocation(BuildingLocation location) {
        this.text = MODID + ".npc.location." + location.getResource().getResourceDomain() + "." + location.getResource().getResourcePath() + ".greeting";
        this.location = location;
    }

    @Override
    public boolean canDisplay(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        BlockPos target = NPCHelper.getCoordinatesForLocation(player, location);
        return target != null && player.getDistanceSq(target) < 32D;
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return TextHelper.getRandomSpeech(npc, text, 10);
    }
}

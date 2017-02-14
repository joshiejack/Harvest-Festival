package joshie.harvest.npcs.greeting;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.IConditionalGreeting;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.town.TownHelper;
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
    public boolean canDisplay(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        BlockPos target = TownHelper.getClosestTownToEntity(player, false).getCoordinatesFor(location);
        return target != null && player.getDistanceSq(target) < 32D;
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        return TextHelper.getRandomSpeech(npc, text, 10);
    }
}

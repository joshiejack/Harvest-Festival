package joshie.harvest.npc.greeting;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.npc.IConditionalGreeting;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.core.util.Text;
import joshie.harvest.npc.NPCHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GreetingLocation implements IConditionalGreeting {
    private final BuildingLocation location;
    private final String text;

    public GreetingLocation(Building building, String area) {
        BuildingImpl location = (BuildingImpl) building;
        this.text = MODID + ".npc.location." + location.getRegistryName().getResourceDomain() + "." + location.getRegistryName().getResourcePath() + ".greeting";
        this.location = new BuildingLocation(location, area);
    }

    @Override
    public boolean canDisplay(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        BlockPos target = NPCHelper.getCoordinatesForLocation(player, location);
        return target != null && player.getDistanceSq(target) < 32D;
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        return Text.getRandomSpeech(npc, text, 10);
    }
}

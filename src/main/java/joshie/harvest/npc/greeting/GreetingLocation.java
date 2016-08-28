package joshie.harvest.npc.greeting;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.npc.IConditionalGreeting;
import joshie.harvest.buildings.Building;
import joshie.harvest.npc.NPCHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GreetingLocation implements IConditionalGreeting {
    private BuildingLocation location;
    private String text;

    public GreetingLocation(IBuilding building, String area) {
        Building location = (Building) building;
        this.text = MODID + ".npc.location." + location.getRegistryName().getResourceDomain() + "." + location.getRegistryName().getResourcePath() + ".greeting";
        this.location = new BuildingLocation(location, area);
    }

    @Override
    public boolean canDisplay(EntityPlayer player) {
        BlockPos target = NPCHelper.getCoordinatesForLocation(player, location);
        return target != null && player.getDistanceSq(target) < 32D;
    }

    @Override
    public int getMaximumAlternatives() {
        return 8;
    }

    @Override
    public String getUnlocalizedText() {
        return text;
    }
}

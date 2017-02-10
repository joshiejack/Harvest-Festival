package joshie.harvest.npcs.greeting;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.IConditionalGreeting;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.npcs.entity.EntityNPCBuilder;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GreetingCarpenter implements IConditionalGreeting<EntityNPCBuilder> {
    @Override
    public String getLocalizedText(EntityPlayer player, EntityNPCBuilder builder, NPC npc) {
        BuildingStage stage = TownHelper.getClosestTownToEntity(builder).getCurrentlyBuilding();
        String toLocalize = MODID + ".npc.builder." + stage.getStage().name().toLowerCase(Locale.ENGLISH);
        return I18n.translateToLocalFormatted(toLocalize, stage.getBuilding().getLocalisedName());
    }

    @Override
    public boolean canDisplay(EntityPlayer player, EntityNPCBuilder builder, NPC npc) {
        BuildingStage stage = TownHelper.getClosestTownToEntity(builder).getCurrentlyBuilding();
        return stage != null && stage.stage != ConstructionStage.FINISHED;
    }

    @Override
    public double getDisplayChance() {
        return 100D;
    }
}

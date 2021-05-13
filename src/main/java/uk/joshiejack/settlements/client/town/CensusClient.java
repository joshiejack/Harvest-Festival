package uk.joshiejack.settlements.client.town;

import com.google.common.collect.Sets;
import uk.joshiejack.settlements.world.town.people.AbstractCensus;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CensusClient extends AbstractCensus {
    private final Set<CustomNPC> customNPCs = Sets.newHashSet();

    public boolean hasResident(ResourceLocation npc) {
        return residents.contains(npc);
    }

    public void setInvitableList(Set<ResourceLocation> invitableList) {
        this.invitableList.clear();
        this.invitableList.addAll(invitableList);
    }

    public void setCustomNPCs(Collection<CustomNPC> custom) {
        customNPCs.clear();
        customNPCs.addAll(custom);
    }

    public Collection<CustomNPC> getCustomNPCs() {
        return customNPCs;
    }

    @Override
    public Collection<ResourceLocation> getCustomNPCKeys() {
        return customNPCs.stream().map(CustomNPC::getRegistryName).collect(Collectors.toList());
    }
}

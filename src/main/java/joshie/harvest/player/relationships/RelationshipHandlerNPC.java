package joshie.harvest.player.relationships;

import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class RelationshipHandlerNPC implements IRelatableDataHandler<NPC> {
    @Override
    public String name() {
        return "npc";
    }

    @Override
    public NPC readFromNBT(NBTTagCompound tag) {
        return NPCRegistry.REGISTRY.getObject(new ResourceLocation(tag.getString("NPC")));
    }

    @Override
    public void writeToNBT(NPC npc, NBTTagCompound tag) {
        tag.setString("NPC", npc.getRegistryName().toString());
    }
}

package joshie.harvest.api.npc.greeting;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class Script extends IForgeRegistryEntry.Impl<Script> {
    public static final IForgeRegistry<Script> REGISTRY = new RegistryBuilder<Script>().setName(new ResourceLocation("harvestfestival", "scripts")).setType(Script.class).setIDRange(0, 32000).create();
    protected String unlocalised;
    private NPC npc;

    public Script(ResourceLocation unlocalised) {
        this.unlocalised = unlocalised.getResourceDomain() + ".script." + unlocalised.getResourcePath().replace("_", ".");
        this.setRegistryName(unlocalised);
        REGISTRY.register(this);
    }

    public Script setNPC(NPC npc) {
        this.npc = npc;
        return this;
    }

    public NPC getNPC() {
        return npc;
    }

    @SuppressWarnings("deprecation")
    public String getLocalized(NPCEntity entity) {
        return I18n.translateToLocal(unlocalised);
    }
}
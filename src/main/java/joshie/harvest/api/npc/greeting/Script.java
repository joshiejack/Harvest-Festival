package joshie.harvest.api.npc.greeting;

import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

public class Script extends IForgeRegistryEntry.Impl<Script> {
    public static final IForgeRegistry<Script> REGISTRY = new RegistryBuilder<Script>().setName(new ResourceLocation("harvestfestival", "scripts")).setType(Script.class).setIDRange(0, 32000).create();
    protected final String unlocalised;
    private NPC npc;

    public Script(ResourceLocation unlocalised) {
        this.unlocalised = unlocalised.getResourceDomain() + ".script." + unlocalised.getResourcePath();
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

    public String getLocalized(EntityAgeable ageable, NPC npc) {
        return I18n.translateToLocal(unlocalised);
    }
}

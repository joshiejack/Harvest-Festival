package joshie.harvest.npcs;

import joshie.harvest.api.npc.INPCHelper;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.npcs.gift.GiftRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

@HFApiImplementation
public class NPCRegistry implements INPCHelper {
    public static final IForgeRegistry<NPC> REGISTRY = new RegistryBuilder<NPC>().setName(new ResourceLocation("harvestfestival", "npcs")).setType(NPC.class).setIDRange(0, 32000).create();
    public static final NPCRegistry INSTANCE = new NPCRegistry();
    private final GiftRegistry gifts = new GiftRegistry();

    private NPCRegistry() {}

    @Override
    public GiftRegistry getGifts() {
        return gifts;
    }

    @Override
    public ItemStack getStackForNPC(NPC npc) {
        return HFNPCs.SPAWNER_NPC.getStackFromObject(npc);
    }

    @Override
    public String getRandomSpeech(NPC npc, String text, int maximumAlternatives, Object... data) {
        return TextHelper.getRandomSpeech(npc, text, maximumAlternatives, data);
    }
}
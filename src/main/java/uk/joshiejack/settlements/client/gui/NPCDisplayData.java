package uk.joshiejack.settlements.client.gui;

import uk.joshiejack.settlements.data.database.NPCLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface NPCDisplayData {
    NPCLoader.NPCClass getNPCClass();
    ResourceLocation getRegistryName();
    String getLocalizedName();
    ItemStack getIcon();
}

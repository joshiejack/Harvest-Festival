package uk.joshiejack.settlements.client.town;

import uk.joshiejack.settlements.client.gui.NPCDisplayData;
import uk.joshiejack.settlements.data.database.NPCLoader;
import uk.joshiejack.settlements.npcs.NPC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class CustomNPC implements NPCDisplayData {
    private final NPC baseNPC;
    private final ResourceLocation uniqueName;
    private final NPCLoader.NPCClass npcClass;
    private final String name;
    private final ItemStack icon;

    @SuppressWarnings("ConstantConditions")
    public CustomNPC(@Nonnull NPC baseNPC, @Nullable ResourceLocation uniqueName, @Nullable NPCLoader.NPCClass npcClass, @Nullable String name, @Nullable String playerSkin, @Nullable String resourceSkin) {
        this.baseNPC = baseNPC;
        this.uniqueName = uniqueName;
        this.npcClass = npcClass;
        this.name = name;
        this.icon = baseNPC.getIcon();
        if (playerSkin != null) {
            icon.getTagCompound().setString("PlayerSkin", playerSkin);
        } else if (resourceSkin != null) {
            icon.getTagCompound().setString("ResourceSkin", resourceSkin);
        }
    }

    @Override
    public NPCLoader.NPCClass getNPCClass() {
        return npcClass == null ? baseNPC.getNPCClass() : npcClass;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return uniqueName == null ? baseNPC.getRegistryName() : uniqueName;
    }

    @Override
    public String getLocalizedName() {
        return name == null ? baseNPC.getLocalizedName() : name;
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomNPC customNPC = (CustomNPC) o;
        return Objects.equals(getRegistryName(), customNPC.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }
}

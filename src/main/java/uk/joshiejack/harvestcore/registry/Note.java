package uk.joshiejack.harvestcore.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.network.notes.PacketUnlockNote;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.data.custom.CustomIcon;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class Note extends PenguinRegistry {
    public static final Map<ResourceLocation, Note> REGISTRY = Maps.newHashMap();
    public static final Multimap<String, Note> CATEGORIES = HashMultimap.create();
    public static final Map<String, ItemStack> ICONS = Maps.newHashMap();
    private final String unlocalizedName;
    private final String unlocalizedText;
    private final String category;
    private final Page.Icon icon;
    private ResourceLocation renderScript;
    private boolean isHidden;
    private boolean isInit;

    public Note(ResourceLocation resource, String category, CustomIcon icon) {
        super(REGISTRY, resource);
        this.category = category;
        this.icon = (icon.gui.isEmpty() && icon.item.isEmpty()) || icon.item.equals("none") ? null:
                icon.gui.equals("default") ? new Page.Icon(GuiElements.getTexture(HarvestCore.MODID, "icons"), icon.x, icon.y) :
                !icon.gui.isEmpty() ? new Page.Icon(new ResourceLocation(icon.gui), icon.x, icon.y) :
                new Page.Icon(StackHelper.getStackFromString(icon.item), 0, 0);
        this.unlocalizedName = resource.getNamespace() + ".note." + resource.getPath() + ".name";
        this.unlocalizedText = resource.getNamespace() + ".note." + resource.getPath() + ".text";
        CATEGORIES.get(category).add(this);
    }

    public void setRenderScript(ResourceLocation renderScript) {
        this.renderScript = renderScript;
    }

    public boolean init() {
        if (renderScript == null || isInit) return true;
        else {
            isInit = true;
            return false;
        }
    }

    public String getText() {
        return unlocalizedText;
    }

    public Note setHidden() {
        this.isHidden = true;
        return this;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public ResourceLocation getRenderScript() {
        return renderScript;
    }

    public String category() {
        return category;
    }

    @Nullable
    public Page.Icon getIcon() {
        return icon;
    }

    public String getLocalizedName() {
        return StringHelper.localize(unlocalizedName);
    }

    public boolean isUnlocked(EntityPlayer player) {
        return PlayerHelper.hasTag(player, "Notes", getRegistryName().toString());
    }

    public void unlock(EntityPlayer player) {
        PlayerHelper.setTag(player, "Notes", getRegistryName().toString());
        if (!player.world.isRemote) {
            PenguinNetwork.sendToClient(new PacketUnlockNote(this), player);
        }
    }

    public boolean isRead(EntityPlayer player) {
        return PlayerHelper.hasTag(player, "ReadNotes", getRegistryName().toString());
    }

    public void read(EntityPlayer player) {
        PlayerHelper.setTag(player, "ReadNotes", getRegistryName().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(getRegistryName(), note.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }
}

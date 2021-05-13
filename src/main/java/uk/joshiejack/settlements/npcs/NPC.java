package uk.joshiejack.settlements.npcs;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.util.Strings;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.client.gui.NPCDisplayData;
import uk.joshiejack.settlements.client.renderer.entity.RenderNPC;
import uk.joshiejack.settlements.data.database.NPCLoader;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.npcs.gifts.GiftQuality;
import uk.joshiejack.settlements.npcs.gifts.GiftRegistry;
import uk.joshiejack.settlements.util.SpeechHelper;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public final class NPC extends PenguinRegistry.Item<NPC> implements NPCDisplayData, NPCInfo {
    private static final ResourceLocation TEXTURE_DEFAULT = new ResourceLocation("textures/entity/steve.png");
    public static final Map<ResourceLocation, NPC> REGISTRY = Maps.newLinkedHashMap();
    public static final NPC NULL_NPC = new NPC(new ResourceLocation(Settlements.MODID, "null")).setOccupation(Strings.EMPTY).setNPCClass(NPCLoader.NPCClass.NULL);
    public static final NPC CUSTOM_NPC = new NPC(new ResourceLocation(Settlements.MODID, "custom")).setOccupation(Strings.EMPTY).setNPCClass(NPCLoader.NPCClass.NULL);
    private static List<NPC> ALL;

    private final Object2IntMap<String> data = new Object2IntOpenHashMap<>();
    private final String unlocalizedGreeting;
    private ResourceLocation lootTable;
    private ResourceLocation script;
    private String localizedName;
    private ResourceLocation skin;
    private String playerSkin;
    private String occupation;
    private NPCLoader.NPCClass clazz;
    private int insideColor;
    private int outsideColor;

    public NPC(ResourceLocation resource) {
        super("npc", REGISTRY, resource);
        this.skin = TEXTURE_DEFAULT;
        this.insideColor = 0xFFFFFF;
        this.outsideColor = 0x000000;
        this.unlocalizedGreeting = unlocalizedKey + ".greeting";
    }

    @Override
    public ItemStack getIcon() {
        return AdventureItems.NPC_SPAWNER.getStackFromResource(getRegistryName());
    }

    public void addData(String name, int value) {
        this.data.put(name, value);
    }

    //Set a resource skin
    public void setSkin(ResourceLocation resource) {
        //TODO: If resource domain is this mod and it's not the joshie npc, then search in the config folder instead
        this.skin = new ResourceLocation(resource.getNamespace(), "textures/entity/" + resource.getPath() + ".png");
    }

    public void setScript(ResourceLocation script) {
        this.script = script;
    }

    //Load a skin from the net
    public void setSkin(String playerSkin) {
        this.playerSkin = playerSkin;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public void setLootTable(ResourceLocation lootTable) {
        this.lootTable = lootTable;
    }

    public String getUnlocalizedKey() {
        return unlocalizedKey;
    }

    public int getData(String name) {
        return data.getInt(name);
    }

    @Nullable
    public ResourceLocation getLootTable() {
        return lootTable;
    }

    public void callScript(String function, Object... params) {
        if (script != null) {
            Scripting.get(script).callFunction(function, params);
        }
    }

    @Nonnull
    public static NPC getNPCFromRegistry(ResourceLocation resource) {
        NPC npc = REGISTRY.get(resource);
        return npc != null ? npc : NULL_NPC;
    }

    public static List<NPC> all() {
        if (ALL == null) {
            ALL = REGISTRY.values().stream().filter((npc -> npc != NULL_NPC)).collect(Collectors.toList());
        }

        return ALL;
    }

    @Override
    public String getOccupation() {
        return occupation;
    }

    public NPC setNPCClass(NPCLoader.NPCClass clazz) {
        this.clazz = clazz;
        return this;
    }

    public NPC setOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public void setColors(int inside, int outside) {
        this.insideColor = inside;
        this.outsideColor = outside;
    }

    public NPCLoader.NPCClass getNPCClass() {
        return clazz;
    }

    public int getInsideColor() {
        return insideColor;
    }

    @Override
    public String getGreeting(Random random) {
        return SpeechHelper.getRandomSpeech(random, getUnlocalizedGreeting(), 20);
    }

    @Override
    public GiftQuality getGiftQuality(ItemStack stack) {
        return GiftRegistry.getQualityForNPC(this, stack);
    }

    @Override
    public String substring(String name) {
        return getUnlocalizedKey() + "." + name;
    }

    public int getOutsideColor() {
        return outsideColor;
    }

    public String getUnlocalizedGreeting() {
        return unlocalizedGreeting;
    }

    @Override
    public String getLocalizedName() {
        return localizedName != null ? localizedName : StringHelper.localize(unlocalizedName);
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getSkin() {
        if (playerSkin != null && skin == null) {
            skin = RenderNPC.getSkinFromUsernameOrUUID(null, playerSkin);
            //We've loaded in the proper skin so let's remove this
            playerSkin = null;
        }

        return skin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NPC npc = (NPC) o;
        return Objects.equals(resource, npc.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource);
    }
}

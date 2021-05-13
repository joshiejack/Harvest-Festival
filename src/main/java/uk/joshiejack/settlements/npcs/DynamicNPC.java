package uk.joshiejack.settlements.npcs;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import uk.joshiejack.settlements.client.renderer.entity.RenderNPC;
import uk.joshiejack.settlements.data.database.NPCLoader;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.item.ItemRandomNPC;
import uk.joshiejack.settlements.npcs.gifts.GiftCategory;
import uk.joshiejack.settlements.npcs.gifts.GiftQuality;
import uk.joshiejack.settlements.npcs.gifts.GiftRegistry;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DynamicNPC implements NPCInfo {
    private final List<Pair<Holder, GiftQuality>> itemOverrides = Lists.newArrayList();
    private final Map<GiftCategory, GiftQuality> categoryOverrides = Maps.newHashMap();
    private final Map<String, String> responses = Maps.newHashMap();
    private final Object2IntMap<String> data = new Object2IntOpenHashMap<>();
    private final List<String> greetings = Lists.newArrayList();
    private final ResourceLocation uniqueID;
    private final NPCLoader.NPCClass npcClass;
    private final String name, occupation, playerSkin, resourceSkin;
    private final int insideColor, outsideColor;
    private final ItemStack icon;
    private final ResourceLocation script;
    private Interpreter it;
    @SideOnly(Side.CLIENT)
    private ResourceLocation skin;

    private DynamicNPC(ResourceLocation uniqueID, NPCLoader.NPCClass npcClass, String name, String occupation, ResourceLocation schedulerScript,
                       @Nullable String playerSkin, @Nullable String resourceSkin, int insideColor, int outsideColor) {
        this.uniqueID = uniqueID;
        this.npcClass = npcClass;
        this.name = name;
        this.occupation = occupation;
        this.script = schedulerScript;
        this.playerSkin = playerSkin;
        this.resourceSkin = resourceSkin;
        this.insideColor = insideColor;
        this.outsideColor = outsideColor;
        this.icon = new ItemStack(AdventureItems.NPC_SPAWNER);
        this.icon.setTagCompound(new NBTTagCompound());
        if (playerSkin != null) {
            icon.getTagCompound().setString("PlayerSkin", playerSkin);
        } else if (resourceSkin != null) {
            icon.getTagCompound().setString("ResourceSkin", resourceSkin);
        }
    }

    public static NPCInfo fromTag(NBTTagCompound custom) {
        DynamicNPC info =  new DynamicNPC(
                new ResourceLocation(custom.getString("UniqueID")),
                NPCLoader.NPCClass.REGISTRY.get(custom.getString("Class")),
                custom.getString("Name"),
                custom.getString("Occupation"),
                custom.hasKey("Script") ? new ResourceLocation(custom.getString("Script")) : null,
                custom.hasKey("PlayerSkin") ? custom.getString("PlayerSkin") : null,
                custom.hasKey("ResourceSkin") ? custom.getString("ResourceSkin") : null,
                custom.getInteger("InsideColor"),
                custom.getInteger("OutsideColor")
        );

        if (custom.hasKey("Gifts")) {
            //Deserialize itemOverrides
            NBTTagCompound tag = custom.getCompoundTag("Gifts");
            if (tag.hasKey("ItemOverrides")) {
                //Build the data
                NBTTagList iOverrideList = tag.getTagList("ItemOverrides", 10);
                for (int i = 0; i < iOverrideList.tagCount(); i++) {
                    NBTTagCompound data = iOverrideList.getCompoundTagAt(i);
                    Holder holder = Holder.getFromString(data.getString("Holder"));
                    GiftQuality quality = GiftQuality.get(data.getString("Quality"));
                    info.itemOverrides.add(Pair.of(holder, quality));
                }
            }

            //Deserialize categoryOverrides
            if (tag.hasKey("CategoryOverrides")) {
                NBTTagList cOverrideList = tag.getTagList("CategoryOverrides", 10);
                for (int i = 0; i < cOverrideList.tagCount(); i++) {
                    NBTTagCompound data = cOverrideList.getCompoundTagAt(i);
                    GiftCategory category = GiftCategory.get(data.getString("Category"));
                    GiftQuality quality = GiftQuality.get(data.getString("Quality"));
                    info.categoryOverrides.put(category, quality);
                }
            }
        }

        //Deserialize responses
        if (custom.hasKey("Speech")) {
            NBTTagCompound tag = custom.getCompoundTag("Speech");
            for (String s: tag.getKeySet()) {
                info.responses.put(s, tag.getString(s));
            }
        }

        //Deserialize data
        if (custom.hasKey("Data")) {
            NBTTagCompound tag = custom.getCompoundTag("Data");
            for (String s: tag.getKeySet()) {
                info.data.put(s, tag.getInteger(s));
            }
        }

        //Deserialize greetings
        custom.getTagList("Greetings", 8).forEach(e -> info.greetings.add(((NBTTagString)e).getString()));
        if (info.greetings.isEmpty()) info.greetings.add("Hello"); //TODO random greetings if there are none
        return info;
    }

    @Override
    public NPCLoader.NPCClass getNPCClass() {
        return npcClass;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return uniqueID;
    }

    @Override
    public String getLocalizedName() {
        return name;
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public String getOccupation() {
        return occupation;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ResourceLocation getSkin() {
        if (skin != null) return skin;
        if (playerSkin != null) {
            skin = RenderNPC.getSkinFromUsernameOrUUID(null, playerSkin);
        } else if (resourceSkin != null) skin = new ResourceLocation(resourceSkin);

        return skin;
    }

    @Override
    public int getOutsideColor() {
        return outsideColor;
    }

    @Override
    public int getInsideColor() {
        return insideColor;
    }

    @Override
    public String getGreeting(Random random) {
        return greetings.get(random.nextInt(greetings.size()));
    }

    @Override
    public GiftQuality getGiftQuality(ItemStack stack) {
        return GiftRegistry.getQualityForNPC(stack, itemOverrides, categoryOverrides);
    }

    @Override
    public String substring(String name) {
        return responses.get(name);
    }

    @Override
    public int getData(String name) {
        return data.get(name);
    }

    @Override
    public void callScript(String function, Object... params) {
        if (script != null) {
            if (it == null) it = Scripting.get(script);
            it.callFunction(function, params);
        }
    }

    public static class Builder {
        private final List<Pair<String, GiftQuality>> itemOverrides = Lists.newArrayList();
        private final Map<GiftCategory, GiftQuality> categoryOverrides = Maps.newHashMap();
        private final Map<GiftQuality, String> giftResponses = Maps.newHashMap();
        private final Object2IntMap<String> data = new Object2IntOpenHashMap<>();
        private final List<String> greetings = Lists.newArrayList();
        private final ResourceLocation uniqueID;
        private String npcClass = "adult";
        private String name = "CustomNPC";
        private String occupation = "villager";
        private String playerSkin = "uk/joshiejack";
        private String resourceSkin = Strings.EMPTY;
        private int insideColor = 0xFFFFFFFF;
        private int outsideColor = 0xFF000000;

        public Builder(ResourceLocation uniqueID) {
            this.uniqueID = uniqueID;
        }

        public Builder(Random rand, ResourceLocation uniqueID) {
            this(uniqueID);
            //Randomiser
            {
                List<String> keys = Lists.newArrayList(NPCLoader.NPCClass.REGISTRY.keySet());
                keys.remove("god"); //No god render
                setClass(keys.get(rand.nextInt(keys.size())));
            }

            //Let's get a random number of items
            int itemOverrides = 5 + rand.nextInt(10);
            for (int i = 0; i < itemOverrides; i++) {
                List<String> keys = Lists.newArrayList(GiftQuality.REGISTRY.keySet());
                addItemOverride(StackHelper.getStringFromStack(StackHelper.getAllItems().get(rand.nextInt(StackHelper.getAllItems().size()))),
                        GiftQuality.get(keys.get(rand.nextInt(keys.size()))));
            }

            //Categories
            int category = 3 + rand.nextInt(5);
            for (int i = 0; i < category; i++) {
                List<String> keys = Lists.newArrayList(GiftQuality.REGISTRY.keySet());
                List<String> keysC = Lists.newArrayList(GiftCategory.REGISTRY.keySet());
                addCategoryOverride(GiftCategory.get(keysC.get(rand.nextInt(keysC.size()))), GiftQuality.get(keys.get(rand.nextInt(keys.size()))));
            }

            //Random Gift thanks?
            for (GiftQuality quality : GiftQuality.REGISTRY.values()) {
                setGiftResponse(quality, "RANDOM TEXT");
            }

            //Random Greeting
            for (int i = 0; i < 10; i++) {
                addGreeting("RANDOM" + i);
            }

            //Random skin
            String name = ItemRandomNPC.NAMES.get(rand.nextInt(ItemRandomNPC.NAMES.size()));
            setName(name).setPlayerSkin(name);

            //Color
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            setInsideColor(randomColor.brighter().getRGB())
                    .setOutsideColor(randomColor.darker().getRGB());
        }

        public void addItemOverride(String string, GiftQuality quality) {
            itemOverrides.add(Pair.of(string, quality));
        }

        public void addCategoryOverride(GiftCategory category, GiftQuality quality) {
            categoryOverrides.put(category, quality);
        }

        public void setGiftResponse(GiftQuality quality, String text) {
            giftResponses.put(quality, text);
        }

        public Builder setData(String name, int value) {
            data.put(name, value);
            return this;
        }

        public void addGreeting(String text) {
            greetings.add(text);
        }

        public Builder setClass(String clazz) {
            this.npcClass = clazz;
            return this;
        }

        public Builder setOccupation(String occupation) {
            this.occupation = occupation;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public void setPlayerSkin(String playerSkin) {
            this.playerSkin = playerSkin;
        }

        public Builder setResourceSkin(String resourceSkin) {
            this.resourceSkin = resourceSkin;
            return this;
        }

        public Builder setInsideColor(int insideColor) {
            this.insideColor = insideColor;
            return this;
        }

        public void setOutsideColor(int outsideColor) {
            this.outsideColor = outsideColor;
        }

        public NBTTagCompound build() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("UniqueID", uniqueID.toString());
            tag.setString("Class", npcClass);
            tag.setString("Name", name);
            tag.setString("Occupation", occupation);
            tag.setString(!resourceSkin.isEmpty() ? "ResourceSkin" : "PlayerSkin", !resourceSkin.isEmpty() ? resourceSkin : playerSkin);
            tag.setInteger("InsideColor", insideColor);
            tag.setInteger("OutsideColor", outsideColor);
            //Gifts
            {
                NBTTagCompound gifts = new NBTTagCompound();
                {
                    NBTTagList iOverrideList = new NBTTagList();
                    itemOverrides.forEach(pair -> {
                        NBTTagCompound override = new NBTTagCompound();
                        override.setString("Holder", pair.getLeft()); //Holder to string?
                        override.setString("Quality", pair.getRight().name());
                        iOverrideList.appendTag(override);
                    });
                    gifts.setTag("ItemOverrides", iOverrideList);
                }
                {
                    NBTTagList cOverrideList = new NBTTagList();
                    categoryOverrides.forEach((key, value) -> {
                        NBTTagCompound override = new NBTTagCompound();
                        override.setString("Category", key.name()); //Holder to string?
                        override.setString("Quality", value.name());
                        cOverrideList.appendTag(override);
                    });

                    gifts.setTag("CategoryOverrides", cOverrideList);
                }
                tag.setTag("Gifts", gifts);
            }

            //Greetings
            {
                NBTTagList greetings = new NBTTagList();
                this.greetings.forEach(g -> greetings.appendTag(new NBTTagString(g)));
                tag.setTag("Greetings", greetings);
            }

            //Thanks
            {
                NBTTagCompound speech = new NBTTagCompound();
                giftResponses.forEach((key, value) -> speech.setString("gift." + key.name(), value));
                tag.setTag("Speech", speech);
            }

            //Data
            {
                NBTTagCompound data = new NBTTagCompound();
                this.data.forEach((k, v) -> data.setInteger(k, this.data.get(k)));

                tag.setTag("Data", data);
            }

            return tag;
        }

        public void randomise() {

        }
    }
}

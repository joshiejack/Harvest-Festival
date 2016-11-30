package joshie.harvest.npc;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.*;
import joshie.harvest.api.npc.INPCRegistry.Age;
import joshie.harvest.api.npc.INPCRegistry.Gender;
import joshie.harvest.api.npc.gift.IGiftHandler;
import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.util.Text;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.greeting.GreetingShop;
import joshie.harvest.npc.item.ItemNPCTool.NPCTool;
import joshie.harvest.shops.Shop;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;

import static joshie.harvest.api.npc.INPCRegistry.Age.ADULT;
import static joshie.harvest.api.npc.INPCRegistry.Age.CHILD;
import static joshie.harvest.core.lib.HFModInfo.*;

public class NPC extends IForgeRegistryEntry.Impl<NPC> implements INPC {
    private final List<IConditionalGreeting> conditionals = new ArrayList<>(256);
    private final EnumMap<Location, BuildingLocation> locations;
    private final String multipleLocalizationKey;
    private final String generalLocalizationKey;
    private final String localizationKey;
    private final ResourceLocation skin;
    private final UUID uuid;

    private final Age age;
    private final Gender gender;
    private final CalendarDate birthday;
    private final int insideColor;
    private final int outsideColor;

    private float height;
    private float offset;
    private IGiftHandler gifts;
    private ISchedule schedule;
    private boolean isBuilder;
    private Shop shop;
    private boolean doesRespawn;
    private boolean alex;
    private IGreeting infoGreeting;
    private ItemStack hasInfo;

    public NPC() {
        this(new ResourceLocation(MODID, "null"), INPCRegistry.Gender.MALE, INPCRegistry.Age.ADULT, new CalendarDate(1, Season.SPRING, 1), 0, 0);
    }

    public NPC(ResourceLocation resource, Gender gender, Age age, CalendarDate birthday, int insideColor, int outsideColor) {
        String MODID = resource.getResourceDomain();
        String name = resource.getResourcePath();
        this.age = age;
        this.gender = gender;
        this.height = 1F;
        this.birthday = birthday;
        this.doesRespawn = true;
        this.hasInfo = null;
        this.insideColor = insideColor;
        this.outsideColor = outsideColor;
        this.localizationKey = MODID + ".npc." + name + ".";
        this.generalLocalizationKey = MODID + ".npc.generic." + age.name().toLowerCase(Locale.ENGLISH) + ".";
        this.skin = new ResourceLocation(MODID, "textures/entity/" + name + ".png");
        this.multipleLocalizationKey = MODID + ".npc." + name + ".greeting";
        this.locations = new EnumMap<>(Location.class);
        this.uuid = UUID.nameUUIDFromBytes(resource.toString().getBytes());
        this.setRegistryName(resource);
        this.setupGifts();
        this.setupSchedules();
        NPCRegistry.REGISTRY.register(this);
    }

    @Override
    public INPC setIsBuilder() {
        isBuilder = true;
        return this;
    }

    @Override
    public INPC setHeight(float height, float offset) {
        this.height = height;
        this.offset = offset;
        return this;
    }

    @Override
    public INPC setShop(IShop shop) {
        this.shop = (Shop) shop;
        setHasInfo(HFNPCs.TOOLS.getStackFromEnum(NPCTool.CLOCK), new GreetingShop(getRegistryName()));
        return this;
    }

    @Override
    public INPC setLocation(Location location, Building building, String home_location) {
        this.locations.put(location, new BuildingLocation(building, home_location));
        return this;
    }

    @Override
    public INPC setAlexSkin() {
        this.alex = true;
        return this;
    }

    @Override
    public INPC setNoRespawn() {
        this.doesRespawn = false;
        return this;
    }

    @Override
    public INPC setGiftHandler(IGiftHandler handler) {
        gifts = handler;
        return this;
    }

    @Override
    public INPC setScheduleHandler(ISchedule handler) {
        schedule = handler;
        return this;
    }

    @Override
    public INPC addGreeting(IConditionalGreeting greeting) {
        this.conditionals.add(greeting);
        return this;
    }

    @Override
    public INPC setHasInfo(ItemStack stack, IGreeting infoGreeting) {
        if (this.hasInfo == null || (stack == null && infoGreeting == null)) {
            this.hasInfo = stack;
            this.infoGreeting = infoGreeting;
        }

        return this;
    }

    public void setupGifts() {
        if (getRegistryName().getResourceDomain().equals(MODID)) {
            try {
                this.gifts = (IGiftHandler) Class.forName(GIFTPATH + WordUtils.capitalize(getRegistryName().getResourcePath())).newInstance();
            } catch (Exception e) {}
        }

        //Backup
        if (this.gifts == null) this.gifts = new IGiftHandler() {};
    }

    public void setupSchedules() {
        if (getRegistryName().getResourceDomain().equals(MODID)) {
            try {
                this.schedule = (ISchedule) Class.forName(SCHEDULEPATH + WordUtils.capitalize(getRegistryName().getResourcePath())).newInstance();
            } catch (Exception e) {}
        }

        if (this.schedule == null) this.schedule = new ISchedule() {}; //Stick to the default
    }

    @Override
    public Age getAge() {
        return age;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public boolean isMarriageCandidate() {
        return age == ADULT;
    }

    public UUID getUUID() {
        return uuid;
    }

    public float getHeight() {
        return height;
    }

    public float getOffset() {
        return offset;
    }

    @Override
    public boolean isBuilder() {
        return isBuilder;
    }

    public Shop getShop() {
        return shop;
    }

    @Override
    public CalendarDate getBirthday() {
        return birthday;
    }

    @Override
    public BuildingLocation getLocation(Location residence) {
        return locations.get(residence);
    }

    //Returns the localized name of this character
    @Override
    public String getLocalizedName() {
        return Text.localize(getRegistryName().getResourceDomain() + ".npc." + getRegistryName().getResourcePath() + ".name");
    }

    @SuppressWarnings("unchecked")
    public String getInfoGreeting(EntityPlayer player, EntityNPC npc) {
        if (infoGreeting == null) return null;
        return infoGreeting.getLocalizedText(player, npc, npc.getNPC());
    }

    public ResourceLocation getSkin() {
        return skin;
    }

    public int getBedtime() {
        return age == CHILD ? 19000 : 23000;
    }

    public String getLocalizationKey() {
        return localizationKey;
    }

    public String getGeneralLocalizationKey() {
        return generalLocalizationKey;
    }

    @Override
    public ResourceLocation getResource() {
        return getRegistryName();
    }

    //Returns the script that this character should say at this point
    @SuppressWarnings("unchecked")
    public String getGreeting(EntityPlayer player, EntityAgeable entity) {
        Collections.shuffle(conditionals);
        for (IConditionalGreeting greeting : conditionals) {
            if (greeting.canDisplay(player, entity, this) && player.worldObj.rand.nextDouble() * 100D < greeting.getDisplayChance()) {
                return greeting.getLocalizedText(player, entity, this);
            }
        }

        return Text.getRandomSpeech(this, multipleLocalizationKey, 100);
    }

    @Override
    public Quality getGiftValue(ItemStack stack) {
        if (gifts == null) return Quality.DECENT; //Always dislike burnt food
        if (stack.getItem() == HFCooking.MEAL && !stack.hasTagCompound()) return Quality.DISLIKE;
        return gifts.getQuality(stack);
    }

    public ItemStack hasInfo() {
        return hasInfo;
    }

    public ISchedule getScheduler() {
        return schedule;
    }

    public int getInsideColor() {
        return insideColor;
    }

    public int getOutsideColor() {
        return outsideColor;
    }

    public boolean isAlexSkin() {
        return alex;
    }

    public boolean respawns() {
        return doesRespawn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        return getRegistryName().equals(((NPC) o).getRegistryName());
    }

    @Override
    public int hashCode() {
        return getRegistryName().hashCode();
    }
}
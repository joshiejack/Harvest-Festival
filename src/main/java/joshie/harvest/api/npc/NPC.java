package joshie.harvest.api.npc;

import com.google.common.collect.Maps;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.CalendarEntry;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.HFRegistry;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.npc.gift.IGiftHandler;
import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.api.npc.greeting.GreetingShop;
import joshie.harvest.api.npc.greeting.IConditionalGreeting;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.npcs.HFNPCs;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

import static joshie.harvest.api.HFApi.npc;
import static joshie.harvest.api.npc.INPCHelper.Age.ADULT;
import static joshie.harvest.core.lib.HFModInfo.MODID;

//TODO: Remove forge registry in 0.7+
//Do not call setRegistryName or anything
//This is only extending the old forge registry for 0.5 > 0.6 compatability reason
public class NPC extends HFRegistry<NPC> implements CalendarEntry {
    public static final Map<ResourceLocation, NPC> REGISTRY = Maps.newHashMap();
    public static final NPC NULL_NPC = new NPC();
    private final List<IConditionalGreeting> conditionals = new ArrayList<>(256);
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

    protected Shop shop;
    private BuildingLocation home;
    private float height;
    private float offset;
    private IGiftHandler gifts;
    private ISchedule schedule;
    private boolean doesRespawn;
    private boolean alex;
    private IInfoButton info;

    private NPC() {
        this(new ResourceLocation(MODID, "null"), INPCHelper.Gender.MALE, INPCHelper.Age.ADULT, new CalendarDate(1, Season.SPRING, 1), 0, 0);
    }

    /** Register a default npc
     * @param resource      the registry name, e.g. harvestfestival:jim
     * @param gender        the gender of the npc, this is only used for specialised greetings
     * @param age           the age group of the npc
     * @param birthday      the date of birth for this npc,
     *                      take note that by default there are 30 days in a season,
     *                      if you use a higher number, this npc will never have
     *                      a birthday unless users change the config value
     * @param insideColor   this is the internal border colour of the npcs chat box
     * @param outsideColor  this is the outer border colour of the npcs chat box **/
    public NPC(ResourceLocation resource, Gender gender, Age age, CalendarDate birthday, int insideColor, int outsideColor) {
        super(resource);
        String MODID = resource.getResourceDomain();
        String name = resource.getResourcePath();
        this.age = age;
        this.gender = gender;
        this.height = 1F;
        this.birthday = birthday;
        this.doesRespawn = true;
        this.insideColor = insideColor;
        this.outsideColor = outsideColor;
        this.localizationKey = MODID + ".npc." + name + ".";
        this.generalLocalizationKey = MODID + ".npc.generic." + age.name().toLowerCase(Locale.ENGLISH) + ".";
        this.skin = new ResourceLocation(MODID, "textures/entity/" + name + ".png");
        this.multipleLocalizationKey = MODID + ".npc." + name + ".greeting";
        this.uuid = UUID.nameUUIDFromBytes(resource.toString().getBytes());
    }

    @Override
    public final Map<ResourceLocation, NPC> getRegistry() {
        return REGISTRY;
    }

    public NPC setHeight(float height, float offset) {
        this.height = height;
        this.offset = offset;
        return this;
    }

    public NPC setShop(Shop shop) {
        this.shop = shop;
        setHasInfo(new GreetingShop(getResource()));
        return this;
    }

    @SuppressWarnings("unused")
    public NPC setAlexSkin() {
        this.alex = true;
        return this;
    }

    @SuppressWarnings("unused")
    public NPC setNoRespawn() {
        this.doesRespawn = false;
        return this;
    }

    public NPC setGiftHandler(IGiftHandler handler) {
        gifts = handler;
        return this;
    }

    public NPC setScheduleHandler(ISchedule handler) {
        schedule = handler;
        return this;
    }

    public NPC addGreeting(IConditionalGreeting greeting) {
        this.conditionals.add(greeting);
        return this;
    }

    public NPC setHasInfo(IInfoButton info) {
        this.info = info;
        return this;
    }

    public NPC setHome(BuildingLocation location) {
        this.home = location;
        return this;
    }

    public Age getAge() {
        return age;
    }

    @Deprecated
    //TODO: Remove in 0.7+
    @SuppressWarnings("unused")
    public Gender getGender() {
        return gender;
    }

    @Nullable
    public BuildingLocation getHome() {
        return home;
    }

    public boolean isMarriageCandidate() {
        return age == ADULT;
    }

    @Deprecated
    //TODO: Remove in 0.7+
    public UUID getUUID() {
        return uuid;
    }

    public float getHeight() {
        return height;
    }

    public float getOffset() {
        return offset;
    }

    @SuppressWarnings("deprecation")
    public boolean isBuilder() {
        return this == HFNPCs.CARPENTER;
    }

    public Shop getShop(World world, BlockPos pos, @Nullable EntityPlayer player) {
        return shop;
    }

    public boolean isShopkeeper() {
        return shop != null;
    }

    public CalendarDate getBirthday() {
        return birthday;
    }

    @SuppressWarnings("deprecation")
    public String getLocalizedName() {
        return I18n.translateToLocal(getResource().getResourceDomain() + ".npc." + getResource().getResourcePath() + ".name");
    }

    public IInfoButton getInfoButton() {
        return info;
    }

    public boolean onClickedInfoButton(EntityPlayer player) {
        return info != null && info.onClicked(this, player);
    }

    public ResourceLocation getSkin() {
        return skin;
    }

    public String getLocalizationKey() {
        return localizationKey;
    }

    public String getGeneralLocalizationKey() {
        return generalLocalizationKey;
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

        return npc.getRandomSpeech(this, multipleLocalizationKey, 100);
    }

    public Quality getGiftValue(ItemStack stack) {
        return gifts == null ? Quality.DECENT : gifts.getQuality(stack);
    }

    public String getInfoTooltip() {
        return info.getTooltip();
    }

    @SideOnly(Side.CLIENT)
    public void drawInfo(GuiScreen screen, int x, int y) {
        info.drawIcon(screen, x, y);
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
    public ItemStack getStackRepresentation() {
        return npc.getStackForNPC(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addTooltipForCalendarEntry(List<String> tooltip) {
        tooltip.add(I18n.translateToLocalFormatted("harvestfestival.npc.tooltip.birthday", getLocalizedName()));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NPC && getResource() != null && getResource().equals(((NPC) o).getResource());
    }

    @Override
    public int hashCode() {
        return getResource() == null? 0 : getResource().hashCode();
    }
}
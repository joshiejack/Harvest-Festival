package joshie.harvest.api.npc;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
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
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

import static joshie.harvest.api.npc.INPCHelper.Age.ADULT;
import static joshie.harvest.core.lib.HFModInfo.MODID;

public class NPC extends IForgeRegistryEntry.Impl<NPC> {
    public static final IForgeRegistry<NPC> REGISTRY = new RegistryBuilder<NPC>().setName(new ResourceLocation("harvestfestival", "npcs")).setType(NPC.class).setIDRange(0, 32000).create();
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

    private BuildingLocation home;
    private float height;
    private float offset;
    private IGiftHandler gifts;
    private ISchedule schedule;
    private Shop shop;
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
     * @param birthday    the date of birth for this npc,
     *                      take note that by default there are 30 days in a season,
     *                      if you use a higher number, this npc will never have
     *                      a birthday unless users change the config value
     * @param insideColor   this is the internal border colour of the npcs chat box
     * @param outsideColor  this is the outer border colour of the npcs chat box
     * @return returns the npc, so you can manipulate it further if you like**/
    public NPC(ResourceLocation resource, Gender gender, Age age, CalendarDate birthday, int insideColor, int outsideColor) {
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
        this.setRegistryName(resource);
        REGISTRY.register(this);
    }

    public NPC setHeight(float height, float offset) {
        this.height = height;
        this.offset = offset;
        return this;
    }

    public NPC setShop(Shop shop) {
        this.shop = shop;
        setHasInfo(new GreetingShop(getRegistryName()));
        return this;
    }

    public NPC setAlexSkin() {
        this.alex = true;
        return this;
    }

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

    public Gender getGender() {
        return gender;
    }

    public BuildingLocation getHome() {
        return home;
    }

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

    @SuppressWarnings("deprecation")
    public boolean isBuilder() {
        return this == HFNPCs.CARPENTER;
    }

    public Shop getShop(World world, BlockPos pos) {
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
        return I18n.translateToLocal(getRegistryName().getResourceDomain() + ".npc." + getRegistryName().getResourcePath() + ".name");
    }

    public IInfoButton getInfoButton() {
        return info;

    }
    @SuppressWarnings("unchecked")
    public String getInfoGreeting(EntityPlayer player, EntityAgeable npc) {
        if (info == null) return null;
        return info.getLocalizedText(player, npc, this);
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

        return HFApi.npc.getRandomSpeech(this, multipleLocalizationKey, 100);
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

    public enum Location {
        HOME, SHOP
    }
}
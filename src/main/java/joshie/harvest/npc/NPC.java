package joshie.harvest.npc;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.IConditionalGreeting;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.Text;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gift.Gifts;
import joshie.harvest.npc.gift.Gifts.Quality;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.npc.NPC.Age.ADULT;
import static joshie.harvest.npc.NPC.Age.CHILD;
import static joshie.harvest.npc.NPC.Gender.FEMALE;
import static joshie.harvest.npc.NPC.Gender.MALE;

public class NPC extends net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl<NPC> implements INPC {
    public enum Gender {
        MALE, FEMALE
    }

    public enum Age {
        CHILD, ADULT, ELDER
    }

    protected List<IConditionalGreeting> conditionals = new ArrayList<IConditionalGreeting>(256);
    protected ArrayList<String> thanks = new ArrayList<String>(6);
    protected String nothanks = "PASS!...";
    protected String accept = "WHAT?";
    protected String reject = "NO!";

    private Age age;
    private Gender gender;
    private float height;
    private float offset;
    private Gifts gifts;
    private boolean isBuilder;
    private IShop shop;
    private ICalendarDate birthday;
    private Pair<ResourceLocation, String> home;
    private boolean doesRespawn;
    private int insideColor;
    private int outsideColor;
    private boolean alex;

    public NPC() {
        this(new ResourceLocation(MODID, "null"), Gender.MALE, Age.ADULT, HFApi.calendar.newDate(1, Season.SPRING, 1), 0, 0);
    }

    public NPC(ResourceLocation resource, Gender gender, Age age, ICalendarDate birthday, int insideColor, int outsideColor) {
        this.gender = gender;
        this.age = age;
        this.height = 1F;
        this.birthday = birthday;
        this.doesRespawn = true;
        this.insideColor = insideColor;
        this.outsideColor = outsideColor;

        String MODID = resource.getResourceDomain();
        String name = resource.getResourcePath();
        String gift = StringUtils.capitalize(name);
        try {
            gifts = (Gifts) Class.forName(HFModInfo.JAVAPATH + "npc.gift.Gifts" + gift).newInstance();
        } catch (Exception ignored) {}

        String key = MODID + ".npc." + name + ".accept";
        accept = Text.localize(key);
        key = MODID + ".npc." + name + ".reject";
        reject = Text.localize(key);
        key = MODID + ".npc." + name + ".gift.reject";
        nothanks = Text.localize(key);
        if (nothanks.equals(key)) nothanks = Text.localize(MODID + ".npc.generic." + age.name().toLowerCase() + ".gift.reject");

        for (int i = 0; i < 6; i++) {
            key = MODID + ".npc." + name + ".gift." + Quality.values()[i].name().toLowerCase();
            String translated = Text.localize(key);
            if (!translated.equals(key)) {
                thanks.add(translated);
            } else {
                key = MODID + ".npc.generic." + age.name().toLowerCase() + ".gift." + Quality.values()[i].name().toLowerCase();
                translated = Text.localize(key);
                thanks.add(translated);
            }
        }

        for (int i = 1; i <= 32; i++) {
            key = MODID + ".npc." + name + ".greeting" + i;
            String greeting = Text.localize(key);
            if (!greeting.equals(key)) {
                conditionals.add(new GreetingGeneric(greeting));
            }

            //Adding Generic Child Greetings
            if (age == CHILD) {
                key = MODID + ".npc.generic.child.greeting" + i;
                greeting = Text.localize(key);
                if (!greeting.equals(key)) {
                    conditionals.add(new GreetingGeneric(greeting));
                }
            } else {
                //Add Generic Adult Greetings
                key = MODID + ".npc.generic.adult.greeting" + i;
                greeting = Text.localize(key);
                if (!greeting.equals(key)) {
                    conditionals.add(new GreetingGeneric(greeting));
                }

                if (gender == MALE) {
                    //Add Generic Male Greetings
                    key = MODID + ".npc.generic.male.greeting" + i;
                    greeting = Text.localize(key);
                    if (!greeting.equals(key)) {
                        conditionals.add(new GreetingGeneric(greeting));
                    }
                } else if (gender == FEMALE) {
                    //Add Generic Female Greetings
                    key = MODID + ".npc.generic.female.greeting" + i;
                    greeting = Text.localize(key);
                    if (!greeting.equals(key)) {
                        conditionals.add(new GreetingGeneric(greeting));
                    }
                }
            }
        }

        Collections.shuffle(conditionals);
        setRegistryName(resource);
        NPCRegistry.REGISTRY.register(this);
    }

    @Override
    public IRelatableDataHandler getDataHandler() {
        return HFApi.relations.getDataHandler("npc");
    }

    @Override
    public NPC setIsBuilder() {
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
        this.shop = shop;
        return this;
    }

    @Override
    public INPC setHome(IBuilding building, String home_location) {
        this.home = Pair.of(HFApi.buildings.getNameForBuilding(building), home_location);
        return this;
    }

    @Override
    public INPC setAlexSkin() {
        this.alex = true;
        return this;
    }

    @Override
    public boolean isChild() {
        return age == CHILD;
    }

    @Override
    public boolean isMarriageCandidate() {
        return age == ADULT;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getOffset() {
        return offset;
    }

    @Override
    public boolean isBuilder() {
        return isBuilder;
    }

    @Override
    public IShop getShop() {
        return shop;
    }

    @Override
    public ICalendarDate getBirthday() {
        return birthday;
    }

    @Override
    public Pair<ResourceLocation, String> getHome() {
        return home;
    }

    //Returns the localized name of this character
    @Override
    public String getLocalizedName() {
        return Text.localize(getRegistryName().getResourceDomain() + ".npc." + getRegistryName().getResourcePath() + ".name");
    }

    @Override
    public int getBedtime() {
        return isChild() ? 19000 : 23000;
    }

    private static class Priority implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            int i1 = ((IConditionalGreeting) o1).getPriority();
            int i2 = ((IConditionalGreeting) o2).getPriority();
            return i1 == i2 ? 0 : i1 > i2 ? 1 : -1;
        }

    }

    //Returns the script that this character should at this point
    @Override
    public String getGreeting(EntityPlayer player) {
        if (conditionals.size() == 0) {
            return "JOSHIE IS STOOPID AND FORGOT WELCOME LANG";
        }

        Collections.shuffle(conditionals);
        Collections.sort(conditionals, new Priority());
        for (IConditionalGreeting greeting : conditionals) {
            if (greeting.canDisplay(player)) {
                return greeting.getText();
            }
        }

        return "Found no greetings";
    }

    @Override
    public Quality getGiftValue(ItemStack stack) {
        return gifts.getQuality(stack);
    }

    @Override
    public String getThanks(Quality value) {
        return thanks.get(value.ordinal());
    }

    @Override
    public String getNoThanks() {
        return nothanks;
    }

    @Override
    public String getAcceptProposal() {
        return accept;
    }

    public String getRejectProposal() {
        return reject;
    }

    @Override
    public int getInsideColor() {
        return insideColor;
    }

    @Override
    public int getOutsideColor() {
        return outsideColor;
    }

    @Override
    public boolean isAlexSkin() {
        return alex;
    }

    public void onContainerClosed(EntityPlayer player, EntityNPC npc) {
        return;
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
package joshie.harvestmoon.npc;

import static joshie.harvestmoon.npc.NPC.Age.ADULT;
import static joshie.harvestmoon.npc.NPC.Age.CHILD;
import static joshie.harvestmoon.npc.NPC.Gender.FEMALE;
import static joshie.harvestmoon.npc.NPC.Gender.MALE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import joshie.harvestmoon.api.buildings.IBuilding;
import joshie.harvestmoon.api.core.IDate;
import joshie.harvestmoon.api.npc.INPC;
import joshie.harvestmoon.api.shops.IShop;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.core.handlers.GuiHandler;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.core.util.Translate;
import joshie.harvestmoon.core.util.generic.Text;
import joshie.harvestmoon.npc.gift.Gifts;
import joshie.harvestmoon.npc.gift.Gifts.Quality;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

public class NPC implements INPC {
    public static enum Gender {
        MALE, FEMALE;
    }

    public static enum Age {
        CHILD, ADULT, ELDER;
    }

    protected ArrayList<String> greetings = new ArrayList(128);
    protected ArrayList<String> thanks = new ArrayList(6);
    protected String accept = "WHAT?";
    protected String reject = "NO!";
    protected String name;
    protected int last;

    private Age age;
    private Gender gender;
    private float height;
    private float offset;
    private Gifts gifts;
    private boolean isBuilder;
    private boolean isMiner;
    private IShop shop;
    private CalendarDate birthday;
    private IBuilding home;
    private String home_location;
    private boolean doesRespawn;

    public NPC(String name, Gender gender, Age age, CalendarDate birthday) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = 1F;
        this.birthday = birthday;
        this.doesRespawn = true;

        String gift = StringUtils.capitalize(name);
        try {
            gifts = (Gifts) Class.forName(HMModInfo.JAVAPATH + "npc.gift.Gifts" + gift).newInstance();
        } catch (Exception e) {}

        String key = HMModInfo.MODPATH + ".npc." + name + ".accept";
        accept = Text.localize(key);
        key = HMModInfo.MODPATH + ".npc." + name + ".reject";
        reject = Text.localize(key);

        for (int i = 0; i < 6; i++) {
            key = HMModInfo.MODPATH + ".npc." + name + ".gift." + Quality.values()[i].name().toLowerCase();
            String translated = Text.localize(key);
            if (!translated.equals(key)) {
                thanks.add(translated);
            } else {
                key = HMModInfo.MODPATH + ".npc.generic." + age.name().toLowerCase() + ".gift." + Quality.values()[i].name().toLowerCase();
                translated = Text.localize(key);
                thanks.add(translated);
            }
        }

        for (int i = 1; i <= 32; i++) {
            key = HMModInfo.MODPATH + ".npc." + name + ".greeting" + i;
            String greeting = Text.localize(key);
            if (!greeting.equals(key)) {
                greetings.add(greeting);
            }

            //Adding Generic Child Greetings
            if (age == CHILD) {
                key = HMModInfo.MODPATH + ".npc.generic.child.greeting" + i;
                greeting = Text.localize(key);
                if (!greeting.equals(key)) {
                    greetings.add(greeting);
                }
            } else {
                //Add Generic Adult Greetings
                key = HMModInfo.MODPATH + ".npc.generic.adult.greeting" + i;
                greeting = Text.localize(key);
                if (!greeting.equals(key)) {
                    greetings.add(greeting);
                }

                if (gender == MALE) {
                    //Add Generic Male Greetings
                    key = HMModInfo.MODPATH + ".npc.generic.male.greeting" + i;
                    greeting = Text.localize(key);
                    if (!greeting.equals(key)) {
                        greetings.add(greeting);
                    }
                } else if (gender == FEMALE) {
                    //Add Generic Female Greetings
                    key = HMModInfo.MODPATH + ".npc.generic.female.greeting" + i;
                    greeting = Text.localize(key);
                    if (!greeting.equals(key)) {
                        greetings.add(greeting);
                    }
                }
            }
        }

        Collections.shuffle(greetings);
    }

    @Override
    public NPC setIsBuilder() {
        isBuilder = true;
        return this;
    }

    @Override
    public NPC setIsMiner() {
        isMiner = true;
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
    public INPC setHome(IBuilding group, String home_location) {
        this.home = group;
        this.home_location = home_location;
        return this;
    }
    
    @Override
    public INPC setNoRespawn() {
        this.doesRespawn = false;
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
    public boolean isMiner() {
        return isMiner;
    }

    @Override
    public IShop getShop() {
        return shop;
    }

    @Override
    public IDate getBirthday() {
        return birthday;
    }

    //Return the name of this character
    @Override
    public String getUnlocalizedName() {
        return name;
    }

    //Returns the localized name of this character
    @Override
    public String getLocalizedName() {
        return Translate.translate("npc." + getUnlocalizedName() + ".name");
    }

    @Override
    public int getBedtime() {
        return isChild()? 19000: 23000;
    }

    //Returns the script that this character should at this point
    @Override
    public String getGreeting() {
        if (greetings.size() == 0) {
            return "JOSHIE IS STOOPID AND FORGOT WELCOME LANG";
        }

        if (last < (greetings.size() - 1)) {
            last++;
        } else last = 0;

        return greetings.get(last);
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
    public String getAcceptProposal() {
        return accept;
    }

    public String getRejectProposal() {
        return reject;
    }

    @Override
    public boolean respawns() {
        return doesRespawn;
    }

    @Override
    public IBuilding getHomeGroup() {
        return home;
    }

    @Override
    public String getHomeLocation() {
        return home_location;
    }

    public void onContainerClosed(EntityPlayer player, EntityNPC npc) {
        return;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        return name.equals(((NPC) o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

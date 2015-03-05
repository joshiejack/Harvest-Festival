package joshie.harvestmoon.npc;

import static joshie.harvestmoon.npc.NPC.Age.ADULT;
import static joshie.harvestmoon.npc.NPC.Age.CHILD;
import static joshie.harvestmoon.npc.NPC.Gender.FEMALE;
import static joshie.harvestmoon.npc.NPC.Gender.MALE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.core.handlers.GuiHandler;
import joshie.harvestmoon.core.helpers.TownHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.core.util.Translate;
import joshie.harvestmoon.core.util.generic.Text;
import joshie.harvestmoon.npc.gift.Gifts;
import joshie.harvestmoon.npc.gift.Gifts.Quality;
import joshie.harvestmoon.shops.ShopInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

public class NPC {
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
    private ShopInventory shop;
    private CalendarDate birthday;
    private BuildingGroup home;
    private String home_location;

    public NPC(String name, Gender gender, Age age, CalendarDate birthday) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = 1F;
        this.birthday = birthday;

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

    public NPC setIsBuilder() {
        isBuilder = true;
        return this;
    }

    public NPC setIsMiner() {
        isMiner = true;
        return this;
    }

    public NPC setHeight(float height, float offset) {
        this.height = height;
        this.offset = offset;
        return this;
    }

    public NPC setShop(ShopInventory inventory) {
        shop = inventory;
        return this;
    }

    public NPC setHome(BuildingGroup group, String home_location) {
        this.home = group;
        this.home_location = home_location;
        return this;
    }

    public boolean isChild() {
        return age == CHILD;
    }

    public boolean isMarriageCandidate() {
        return age == ADULT;
    }

    public float getHeight() {
        return height;
    }

    public float getOffset() {
        return offset;
    }

    public boolean isBuilder() {
        return isBuilder;
    }

    public boolean isMiner() {
        return isMiner;
    }

    public ShopInventory getShop() {
        return shop;
    }

    public CalendarDate getBirthday() {
        return birthday;
    }

    public EntityNPC getEntity(UUID owning_player, World world) {
        if (isBuilder()) {
            return new EntityNPCBuilder(owning_player, world, this);
        } else if (isMiner()) {
            return new EntityNPCMiner(owning_player, world, this);
        } else if (shop != null) {
            return new EntityNPCShopkeeper(owning_player, world, this);
        } else return new EntityNPC(owning_player, world, this);
    }

    public int getGuiID(World world, boolean isSneaking) {
        return shop != null && shop.isOpen(world) ? GuiHandler.SHOP : (isSneaking) ? GuiHandler.GIFT : GuiHandler.NPC;
    }

    //Return the name of this character
    public String getUnlocalizedName() {
        return name;
    }

    //Returns the localized name of this character
    public String getLocalizedName() {
        return Translate.translate("npc." + getUnlocalizedName() + ".name");
    }

    public int getBedtime() {
        return isChild()? 19000: 23000;
    }

    /** CAN AND WILL RETURN NULL, IF THE UUID COULD NOT BE FOUND **/
    public WorldLocation getHomeLocation(EntityNPC entity) {
        UUID owner_uuid = entity.owning_player;
        if (home == null || home_location == null) return null;
        return TownHelper.getLocationFor(owner_uuid, home, home_location);
    }

    //Returns the script that this character should at this point
    public String getGreeting() {
        if (greetings.size() == 0) {
            return "JOSHIE IS STOOPID AND FORGOT WELCOME LANG";
        }

        if (last < (greetings.size() - 1)) {
            last++;
        } else last = 0;

        return greetings.get(last);
    }

    public Quality getGiftValue(ItemStack stack) {
        return gifts.getQuality(stack);
    }

    public String getThanks(Quality value) {
        return thanks.get(value.ordinal());
    }

    public String getAcceptProposal() {
        return accept;
    }

    public String getRejectProposal() {
        return reject;
    }

    public boolean respawns() {
        return true;
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

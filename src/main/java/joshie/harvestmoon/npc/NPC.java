package joshie.harvestmoon.npc;

import static joshie.harvestmoon.npc.NPC.Age.CHILD;
import static joshie.harvestmoon.npc.NPC.Gender.FEMALE;
import static joshie.harvestmoon.npc.NPC.Gender.MALE;

import java.util.ArrayList;
import java.util.Collections;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.handlers.GuiHandler;
import joshie.harvestmoon.npc.gift.Gifts;
import joshie.harvestmoon.npc.gift.Gifts.GiftQuality;
import joshie.harvestmoon.shops.ShopInventory;
import joshie.harvestmoon.util.Translate;
import joshie.harvestmoon.util.generic.Text;
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

    protected ArrayList<String> greetings = new ArrayList(32);
    protected ArrayList<String> thanks = new ArrayList(6);
    protected String name;
    protected int last;

    private Age age;
    private Gender gender;
    private Gifts gifts;
    private boolean isBuilder;
    private boolean isMiner;
    private ShopInventory shop;
    private CalendarDate birthday;

    public NPC(String name, Gender gender, Age age, CalendarDate birthday) {
        this.name = name;
        this.gender = gender;
        this.age = age;

        String gift = StringUtils.capitalize(name);
        try {
            gifts = (Gifts) Class.forName("joshie.harvestmoon.npc.gift.Gifts" + gift).newInstance();
        } catch (Exception e) {}

        for (int i = 0; i < 6; i++) {
            String key = "hm.npc." + name + ".gift." + GiftQuality.values()[i].name().toLowerCase();
            String translated = Text.localize(key);
            if (!translated.equals(key)) {
                thanks.add(translated);
            } else {
                key = "hm.npc.generic." + age.name().toLowerCase() + ".gift." + GiftQuality.values()[i].name().toLowerCase();
                translated = Text.localize(key);
                thanks.add(translated);
            }
        }

        for (int i = 1; i <= 32; i++) {
            String key = "hm.npc." + name + ".greeting" + i;
            String greeting = Text.localize(key);
            if (!greeting.equals(key)) {
                greetings.add(greeting);
            }

            //Adding Generic Child Greetings
            if (age == CHILD) {
                key = "hm.npc.generic.child.greeting" + i;
                greeting = Text.localize(key);
                if (!greeting.equals(key)) {
                    greetings.add(greeting);
                }
            } else {
                //Add Generic Adult Greetings
                key = "hm.npc.generic.adult.greeting" + i;
                greeting = Text.localize(key);
                if (!greeting.equals(key)) {
                    greetings.add(greeting);
                }

                if (gender == MALE) {
                    //Add Generic Male Greetings
                    key = "hm.npc.generic.male.greeting" + i;
                    greeting = Text.localize(key);
                    if (!greeting.equals(key)) {
                        greetings.add(greeting);
                    }
                } else if (gender == FEMALE) {
                    //Add Generic Female Greetings
                    key = "hm.npc.generic.female.greeting" + i;
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

    public NPC setShop(ShopInventory inventory) {
        shop = inventory;
        return this;
    }

    public boolean isChild() {
        return age == CHILD;
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

    public EntityNPC getEntity(World world, int x, int y, int z) {
        if (isBuilder()) {
            return new EntityNPCBuilder(world, this);
        } else if (isMiner()) {
            return new EntityNPCMiner(world, this);
        } else if (shop != null) {
            return new EntityNPCShopkeeper(world, this).setWorkLocation(x, y, z);
        } else return new EntityNPC(world, this);
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

    public GiftQuality getGiftValue(ItemStack stack) {
        return gifts.getValue(stack);
    }

    public String getThanks(GiftQuality value) {
        return thanks.get(value.ordinal());
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

package joshie.harvestmoon.entities;

import static joshie.harvestmoon.entities.NPC.Age.CHILD;
import static joshie.harvestmoon.entities.NPC.Gender.FEMALE;
import static joshie.harvestmoon.entities.NPC.Gender.MALE;

import java.util.ArrayList;
import java.util.Collections;

import joshie.harvestmoon.handlers.GuiHandler;
import joshie.harvestmoon.shops.ShopInventory;
import joshie.harvestmoon.util.Translate;
import joshie.harvestmoon.util.generic.Text;
import net.minecraft.world.World;

public class NPC {
    public static enum Gender {
        MALE, FEMALE;
    }

    public static enum Age {
        CHILD, ADULT, ELDER;
    }

    protected ArrayList<String> greetings = new ArrayList();
    protected String name;
    protected int last;

    private Age age;
    private Gender gender;
    private boolean isBuilder;
    private ShopInventory shop;

    public NPC(String name, Gender gender, Age age) {
        this.name = name;
        this.gender = gender;
        this.age = age;

        for (int i = 1; i < 32; i++) {
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

    public ShopInventory getShop() {
        return shop;
    }

    public EntityNPC getEntity(World world) {
        if (isBuilder()) {
            return new EntityNPCBuilder(world, this);
        } else if (shop != null) {
            return new EntityNPCShopkeeper(world, this);
        } else return new EntityNPC(world, this);
    }

    public int getGuiID(World world) {
        return shop != null && shop.isOpen(world) ? GuiHandler.SHOP : GuiHandler.NPC;
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
    public String getScript() {
        if (greetings.size() == 0) {
            return "JOSHIE IS STOOPID AND FORGOT WELCOME LANG";
        }

        if (last < (greetings.size() - 1)) {
            last++;
        } else last = 0;

        return greetings.get(last);
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

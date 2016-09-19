package joshie.harvest.plugins;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static joshie.harvest.api.calendar.Weekday.*;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@ObjectHolder("Aquaculture")
@HFLoader(mods = "Aquaculture")
public class Aquaculture {
    public static final Item fish = null;
    public static final Item fishing_rod = null;
    public static final Item iron_fishing_rod = null;
    public static final Item gold_fishing_rod = null;
    public static final Item diamond_fishing_rod = null;
    public static IShop BAITSHOP;

    private static boolean isFish(int damage) {
        return damage != 19 && damage != 17 && damage != 13 && damage != 18 && damage != 14 && damage != 15 && damage != 16;
    }

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 0), 200); //Bluegill
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 1), 200); //Perch
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 32), 200); //Boulti
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 19), 200); //Leech
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 9), 200); //Red Grouper
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 23), 350); //Arapaima
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 31), 350); //Capitaine
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 7), 350); //Carp
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 17), 350); //Frog
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 27), 350); //Halibut
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 13), 400); //Shark
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 18), 400); //Turtle
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 14), 400); //Whale
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 33), 250); //Bagrid
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 36), 250); //Brown Shroom
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 2), 250); //Gar
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 29), 250); //Rainbow Trout
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 35), 250); //Red Shroom
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 3), 300); //Bass
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 5), 300); //Brown Trout
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 24), 300); //Cod
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 4), 300); //Muskellunge
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 28), 300); //Pink Salmon
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 10), 300); //Salmon
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 11), 300); //Tuna
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 6), 330); //Catfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 12), 330); //Swordfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 22), 330); //Tambaquit
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 21), 230); //Electric Eel
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 25), 230); //Pollock
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 8), 50); //Blowfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 20), 50); //Piranha
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 34), 50); //Syndotis
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 26), 50); //Herring
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 16), 150); //Jellyfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 15), 150); //Squid
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 30), 130); //Blackfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 37), 30); //Goldfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 38), 1); //Bones
        Ingredient fishIngredient = Ingredient.INGREDIENTS.get("fish");
        for (int i = 0; i <= 37; i++) {
            if (isFish(i)) {
                HFApi.cooking.register(new ItemStack(fish, 1, i), fishIngredient);
            }
        }

        BAITSHOP = HFApi.shops.newShop(new ResourceLocation(MODID, "baitshop"), HFNPCs.FISHERMAN);
        BAITSHOP.addItem(1000L, new ItemStack(fishing_rod));
        BAITSHOP.addItem(15000L, new ItemStack(iron_fishing_rod));
        BAITSHOP.addItem(25000L, new ItemStack(gold_fishing_rod));
        BAITSHOP.addItem(50000L, new ItemStack(diamond_fishing_rod));
        BAITSHOP.addOpening(TUESDAY, 13000, 19000).addOpening(WEDNESDAY, 13000, 19000).addOpening(THURSDAY, 13000, 19000).addOpening(FRIDAY, 13000, 19000);
    }
}

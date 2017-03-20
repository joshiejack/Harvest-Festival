package joshie.harvest.plugins;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.plugins.crafttweaker.wrappers.RequirementOreWrapper;
import joshie.harvest.shops.purchasable.PurchasableMaterials;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import javax.annotation.Nonnull;

import static joshie.harvest.shops.HFShops.BAITSHOP;

@ObjectHolder("Aquaculture")
@HFLoader(mods = "Aquaculture")
public class Aquaculture {
    public static final Item loot = null;
    public static final Item fish = null;
    public static final Item fishing_rod = null;
    public static final Item iron_fishing_rod = null;
    public static final Item gold_fishing_rod = null;
    public static final Item diamond_fishing_rod = null;

    private static boolean isFish(int damage) {
        return damage != 19 && damage != 17 && damage != 13 && damage != 18 && damage != 14 && damage != 15 && damage != 16;
    }

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 0), 50); //Bluegill
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 1), 50); //Perch
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 32), 50); //Boulti
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 19), 50); //Leech
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 9), 50); //Red Grouper
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 23), 85); //Arapaima
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 31), 85); //Capitaine
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 7), 85); //Carp
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 17), 85); //Frog
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 27), 85); //Halibut
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 13), 100); //Shark
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 18), 100); //Turtle
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 14), 100); //Whale
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 33), 60); //Bagrid
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 36), 60); //Brown Shroom
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 2), 60); //Gar
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 29), 60); //Rainbow Trout
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 35), 60); //Red Shroom
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 3), 75); //Bass
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 5), 75); //Brown Trout
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 24), 75); //Cod
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 4), 75); //Muskellunge
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 28), 75); //Pink Salmon
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 10), 75); //Salmon
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 11), 75); //Tuna
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 6), 80); //Catfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 12), 80); //Swordfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 22), 80); //Tambaquit
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 21), 55); //Electric Eel
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 25), 55); //Pollock
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 8), 30); //Blowfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 20), 30); //Piranha
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 34), 30); //Syndotis
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 26), 30); //Herring
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 16), 40); //Jellyfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 15), 40); //Squid
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 30), 35); //Blackfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 37), 10); //Goldfish
        HFApi.shipping.registerSellable(new ItemStack(fish, 1, 38), 1); //Bones
        Ingredient fishIngredient = Ingredient.INGREDIENTS.get("fish");
        for (int i = 0; i <= 37; i++) {
            ItemStack stack = new ItemStack(fish, 1, i);
            HFApi.fishing.registerForFishingCollection(stack);
            if (isFish(i)) {
                HFApi.fishing.registerAsBreedable(stack, 3);
                HFApi.cooking.register(stack, fishIngredient);
            } else HFApi.fishing.registerAsBreedable(stack, 5);
        }

        HFApi.fishing.registerBait(new ItemStack(fish, 1, 19));
        HFApi.shipping.registerSellable(new ItemStack(loot, 1, 2), 0);
        HFApi.shipping.registerSellable(new ItemStack(loot, 1, 2), 1);

        BAITSHOP.addPurchasable(new PurchasableMaterials(1000L, new ItemStack(fishing_rod), new RequirementOreWrapper("stickWood", 1)));
        BAITSHOP.addPurchasable(new PurchasableMaterials(1000L, new ItemStack(gold_fishing_rod), new RequirementOreWrapper("ingotGold", 1)) {
            @Override
            public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
                CalendarDate date = HFApi.calendar.getDate(world);
                return (date.getYear() >= 1 || date.getSeason().ordinal() >= 1);
            }
        });
        BAITSHOP.addPurchasable(new PurchasableMaterials(1500L, new ItemStack(iron_fishing_rod), new RequirementOreWrapper("ingotIron", 1)){
            @Override
            public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
                CalendarDate date = HFApi.calendar.getDate(world);
                return (date.getYear() >= 1 || date.getSeason().ordinal() >= 2);
            }
        });
        BAITSHOP.addPurchasable(new PurchasableMaterials(5000L, new ItemStack(diamond_fishing_rod), new RequirementOreWrapper("gemDiamond", 1)){
            @Override
            public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
                CalendarDate date = HFApi.calendar.getDate(world);
                return (date.getYear() >= 1 || date.getSeason().ordinal() >= 3);
            }
        });
    }
}

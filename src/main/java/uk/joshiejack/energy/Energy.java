package uk.joshiejack.energy;

import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Energy.MODID, name = "Energy", version = "@ENERGY_VERSION@", dependencies = "required-after:penguinlib")
public class Energy {
    public static final String MODID = "energy";

    private void adjustHungerAmount(Item item, int amount) {
        ReflectionHelper.setPrivateFinalValue(ItemFood.class, (ItemFood) item, amount, "healAmount", "field_77853_b");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (EnergyConfig.modifyVanillaFoods) {
            adjustHungerAmount(Items.BAKED_POTATO, 3);
            adjustHungerAmount(Items.CARROT, 2);
            adjustHungerAmount(Items.APPLE, 2);
            adjustHungerAmount(Items.BREAD, 3);
            adjustHungerAmount(Items.COOKIE, 1);
            adjustHungerAmount(Items.MELON, 1);
            adjustHungerAmount(Items.COOKED_CHICKEN, 5);
            adjustHungerAmount(Items.COOKED_BEEF, 6);
            adjustHungerAmount(Items.COOKED_PORKCHOP, 6);
            adjustHungerAmount(Items.SPIDER_EYE, 1);
            adjustHungerAmount(Items.ROTTEN_FLESH, 2);
            adjustHungerAmount(Items.PUMPKIN_PIE, 6);
            adjustHungerAmount(Items.RABBIT, 2);
            adjustHungerAmount(Items.RABBIT_STEW, 8);
            adjustHungerAmount(Items.BEETROOT_SOUP, 5);
            adjustHungerAmount(Items.CHORUS_FRUIT, 2);
        }

        if (EnergyConfig.stackableSoups) {
            Items.BEETROOT_SOUP.setMaxStackSize(64);
            Items.MUSHROOM_STEW.setMaxStackSize(64);
        }
    }
}

package uk.joshiejack.piscary;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import uk.joshiejack.penguinlib.creativetab.PenguinTab;
import uk.joshiejack.piscary.loot.PiscaryLootTables;
import uk.joshiejack.piscary.item.ItemFish;
import uk.joshiejack.piscary.item.PiscaryItems;

@Mod.EventBusSubscriber
@Mod(modid = Piscary.MODID, name = "Piscary", version = "@PISCARY_VERSION@", dependencies = "required-after:penguinlib")
public class Piscary {
    public static final String MODID = "piscary";
    @SuppressWarnings("ConstantConditions")
    public static final CreativeTabs TAB = new PenguinTab(MODID, () ->  PiscaryItems.FISH.getStackFromEnum(ItemFish.Fish.PUPFISH));

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PiscaryLootTables.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PiscaryItems.init();
    }
}

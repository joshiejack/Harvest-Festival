package uk.joshiejack.husbandry.item;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import net.minecraft.util.ResourceLocation;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class ItemFabric extends ItemMulti<ItemFabric.Fabric> {
    public ItemFabric() {
        super(new ResourceLocation(MODID, "fabric"), Fabric.class);
        setCreativeTab(Husbandry.TAB);
    }

    public enum Fabric {
        SMALL_WOOL, MEDIUM_WOOL, LARGE_WOOL,
        SMALL_YARN, MEDIUM_YARN, LARGE_YARN,
        SMALL_FINE_YARN, MEDIUM_FINE_YARN, LARGE_FINE_YARN
    }
 }

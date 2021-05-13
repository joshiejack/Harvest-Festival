package uk.joshiejack.husbandry.item;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.penguinlib.item.base.ItemBaseSickle;
import net.minecraft.util.ResourceLocation;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class ItemSickle extends ItemBaseSickle {
    public ItemSickle() {
        super(new ResourceLocation(MODID, "sickle"));
        setCreativeTab(Husbandry.TAB);
    }
}

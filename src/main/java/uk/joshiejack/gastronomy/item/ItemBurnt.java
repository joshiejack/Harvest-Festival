package uk.joshiejack.gastronomy.item;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import net.minecraft.util.ResourceLocation;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

public class ItemBurnt extends ItemMulti<Appliance> {
    public ItemBurnt() {
        super(new ResourceLocation(MODID, "burnt"), Appliance.class);
        setCreativeTab(Gastronomy.TAB);
    }
 }

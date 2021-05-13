package uk.joshiejack.husbandry.item;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import net.minecraft.util.ResourceLocation;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class ItemFeed extends ItemMulti<ItemFeed.Feed> {
    public ItemFeed() {
        super(new ResourceLocation(MODID, "feed"), Feed.class);
        setCreativeTab(Husbandry.TAB);
    }

    public enum Feed {
        FODDER, BIRD_FEED, CAT_FOOD, DOG_FOOD, RABBIT_FOOD, SLOP
    }
 }

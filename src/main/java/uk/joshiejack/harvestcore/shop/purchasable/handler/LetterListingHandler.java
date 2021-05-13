package uk.joshiejack.harvestcore.shop.purchasable.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.harvestcore.registry.letter.LetterGift;
import uk.joshiejack.harvestcore.world.PostalOffice;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.item.base.ItemSpecial;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

@PenguinLoader("letter")
public class LetterListingHandler extends ListingHandler<Letter> {
    private static ItemStack[] MAIL = null;

    @Override
    public String getType() {
        return "letter";
    }

    @Override
    public String getDisplayName(Letter letter) {
        return StringHelper.localize(letter.getUnlocalizedName());
    }

    @Override
    public ItemStack[] createIcon(Letter letter) {
        if (letter instanceof LetterGift) {
            return new ItemStack[] { ((LetterGift)letter).getStack() };
        } else {
            if (MAIL == null) {
                MAIL =  new ItemStack[] { PenguinItems.SPECIAL.getStackFromEnum(ItemSpecial.Special.MAIL) };
            }

            return MAIL;
        }
    }

    @Override
    public void purchase(EntityPlayer player, Letter letter) {
        PostalOffice.send(player.world, player, letter);
    }

    @Override
    public Letter getObjectFromDatabase(Database database, String data) {
        return Letter.REGISTRY.get(new ResourceLocation(data));
    }

    @Override
    public String getStringFromObject(Letter letter) {
        return letter.getRegistryName().toString();
    }

    @Override
    public boolean isValid(Letter created) {
        return Letter.REGISTRY.values().contains(created);
    }

    @Override
    public String getValidityError() {
        return "Letter does not exist";
    }
}

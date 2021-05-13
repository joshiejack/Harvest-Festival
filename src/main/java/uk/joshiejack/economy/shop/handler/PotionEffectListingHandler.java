package uk.joshiejack.economy.shop.handler;

import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

@PenguinLoader("potion")
public class PotionEffectListingHandler extends ListingHandler<PotionEffect> {
    @Override
    public String getType() {
        return "potion";
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public PotionEffect getObjectFromDatabase(Database database, String data) {
        Row row = database.table("potion_effects").fetch_where("id=" + data);
        Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(row.get("potion")));
        int duration = row.get("duration");
        int amplifier = row.get("amplifier");
        boolean ambient = row.get("is_ambient");
        boolean particles = row.get("show_particles");
        return new PotionEffect(potion, duration, amplifier, ambient, particles);
    }

    @Override
    public String getStringFromObject(PotionEffect effect) {
        return effect.getPotion().getRegistryName().toString() + " " + effect.getDuration() + " "
                + effect.getAmplifier() + " " + effect.getIsAmbient() + " " + effect.doesShowParticles();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isValid(PotionEffect created) {
        return created.getPotion() != null;
    }

    @Override
    public String getValidityError() {
        return "Potion does not exist";
    }

    @Override
    public String getDisplayName(PotionEffect resource) {
        return StringHelper.localize(resource.getEffectName());
    }


    @Override
    public ItemStack[] createIcon(PotionEffect resource) {
        return new ItemStack[] { new ItemStack(Items.POTIONITEM) };
    }

    @Override
    public void purchase(EntityPlayer player, PotionEffect effect) {
        if (!player.world.isRemote) {
            player.addPotionEffect(new PotionEffect(effect)); //COPY!
        }
    }
}

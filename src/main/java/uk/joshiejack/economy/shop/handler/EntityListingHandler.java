package uk.joshiejack.economy.shop.handler;

import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.penguinlib.item.ItemEntity;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;

@PenguinLoader("entity") //TODO: More powerful options for the entity
public class EntityListingHandler extends ListingHandler<ResourceLocation> {
    @Override
    public String getType() {
        return "entity";
    }

    @Override
    public ResourceLocation getObjectFromDatabase(Database database, String data) {
        return new ResourceLocation(data);
    }

    @Override
    public String getStringFromObject(ResourceLocation resourceLocation) {
        return resourceLocation.toString();
    }

    @Override
    public boolean isValid(ResourceLocation created) {
        return GameData.getEntityRegistry().containsKey(created);
    }

    @Override
    public String getValidityError() {
        return "Entity does not exist";
    }

    @Override
    public String getDisplayName(ResourceLocation resource) {
        return ItemEntity.fromResource(resource).getName();
    }


    @Override
    public ItemStack[] createIcon(ResourceLocation resource) {
        ItemStack stack = new ItemStack(PenguinItems.ENTITY);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("Entity", resource.toString());
        return new ItemStack[] { stack };
    }

    @Override
    public void purchase(EntityPlayer player, ResourceLocation resource) {
        if (!player.world.isRemote) {
            Entity entity = EntityList.createEntityByIDFromName(resource, player.world);
            if (entity != null) {
                entity.setPosition(player.posX, player.posY, player.posZ);
                player.world.spawnEntity(entity);
            }
        }
    }
}

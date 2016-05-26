package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.WHITE;

public class PurchaseableEntity implements IPurchaseable {
    private ItemStack product;
    private Class<? extends Entity> eClass;
    private long cost;
    private boolean lead;

    /**
     * If lead is true, entity spawns with a lead, otherwise, entity spawns mounting the player
     **/
    public PurchaseableEntity(Class<? extends Entity> clazz, long cost, ItemStack render, boolean lead) {
        this.product = render;
        this.eClass = clazz;
        this.cost = cost;
        this.lead = lead;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return canBuy(world, player);
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public ItemStack getDisplayStack() {
        return product;
    }

    @Override
    public void addTooltip(List<String> list) {
        list.add(WHITE + product.getDisplayName());
        if (!lead) {
            list.add(Text.translate("check.head"));
        } else list.add(Text.translate("check.lead"));
    }

    public Entity createEntity(World world) {
        Entity entity = null;
        try {
            if (eClass != null) {
                entity = eClass.getConstructor(new Class[]{World.class}).newInstance(world);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return entity;
    }

    @Override
    public boolean onPurchased(EntityPlayer player) {
        Entity theEntity = createEntity(player.worldObj);
        if (theEntity != null) {
            theEntity.setPosition(player.posX, player.posY, player.posZ);
            if (!lead) {
                player.worldObj.spawnEntityInWorld(theEntity);
                theEntity.startRiding(player);
            } else {
                ((EntityAnimal) theEntity).setLeashedToEntity(player, true);
                ((IAnimalTracked) theEntity).getData().setOwner(player);
                player.worldObj.spawnEntityInWorld(theEntity);
            }
        }

        return false;
    }
}
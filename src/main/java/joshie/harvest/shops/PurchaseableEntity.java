package joshie.harvest.shops;

import java.util.List;

import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableEntity implements IPurchaseable {
    private ItemStack product;
    private Class eClass;
    private long cost;
    private boolean lead;

    /** If lead is true, entity spawns with a lead, otherwise, entity spawns mounting the player  **/
    public PurchaseableEntity(Class clazz, long cost, ItemStack render, boolean lead) {
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
    public void addTooltip(List list) {
        list.add(Text.WHITE + product.getDisplayName());
        if (!lead) {
            list.add(Translate.translate("check.head"));
        } else list.add(Translate.translate("check.lead"));
    }

    public Entity createEntity(World world) {
        Entity entity = null;
        try {
            if (eClass != null) {
                entity = (Entity) eClass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { world });
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
                theEntity.mountEntity(player);
            } else {
                EntityLeashKnot leash = EntityLeashKnot.func_110129_a(player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
                ((EntityAnimal) theEntity).setLeashedToEntity(player, true);
                player.worldObj.spawnEntityInWorld(theEntity);
            }
        }

        return false;
    }
}

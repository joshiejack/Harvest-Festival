package joshie.harvestmoon.shops;

import java.util.List;

import joshie.harvestmoon.api.shops.IPurchaseable;
import joshie.harvestmoon.core.util.Translate;
import joshie.harvestmoon.core.util.generic.Text;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableEntity implements IPurchaseable {
    private ItemStack product;
    private String entity;
    private long cost;
    private boolean lead;

    /** If lead is true, entity spawns with a lead, otherwise, entity spawns mounting the player  **/
    public PurchaseableEntity(String entity, long cost, ItemStack render, boolean lead) {
        this.product = render;
        this.entity = entity;
        this.cost = cost;
        this.lead = lead;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return true;
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

    @Override
    public boolean onPurchased(EntityPlayer player) {
        Entity theEntity = EntityList.createEntityByName(entity, player.worldObj);
        theEntity.setPosition(player.posX, player.posY, player.posZ);

        if (!lead) {
            player.worldObj.spawnEntityInWorld(theEntity);
            theEntity.mountEntity(player);
        } else {
            EntityLeashKnot leash = EntityLeashKnot.func_110129_a(player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            ((EntityLiving)theEntity).setLeashedToEntity(player, true);
            player.worldObj.spawnEntityInWorld(theEntity);
        }
        
        return false;
    }
}

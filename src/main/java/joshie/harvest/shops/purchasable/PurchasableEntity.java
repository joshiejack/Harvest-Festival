package joshie.harvest.shops.purchasable;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.world.World;

import java.util.List;
import java.util.Locale;

import static net.minecraft.util.text.TextFormatting.WHITE;

public class PurchasableEntity implements IPurchasable {
    private final String resource;
    private final ItemStack product;
    private final Class<? extends Entity> eClass;
    private final long cost;
    private final boolean lead;

    /**
     * If lead is true, entity spawns with a lead, otherwise, entity spawns mounting the player
     **/
    public PurchasableEntity(Class<? extends Entity> clazz, long cost, ItemStack render, boolean lead) {
        this.product = render;
        this.eClass = clazz;
        this.cost = cost;
        this.lead = lead;
        this.resource = clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        if (!lead) {
            return player.getPassengers().size() == 0;
        }

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
            list.add(TextHelper.translate("check.head"));
        } else list.add(TextHelper.translate("check.lead"));
    }

    public EntityLiving createEntity(World world) {
        EntityLiving entity = null;
        try {
            if (eClass != null) {
                entity = (EntityLiving) eClass.getConstructor(new Class[]{World.class}).newInstance(world);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return entity;
    }

    @Override
    public boolean onPurchased(EntityPlayer aPlayer) {
        if (!aPlayer.worldObj.isRemote) {
            EntityPlayerMP player = (EntityPlayerMP)aPlayer;
            EntityLiving theEntity = createEntity(player.worldObj);
            if (theEntity != null) {
                theEntity.setPosition(player.posX, player.posY, player.posZ);
                if (!lead) {
                    theEntity.startRiding(player, true);
                    player.worldObj.spawnEntityInWorld(theEntity);
                    player.connection.sendPacket(new SPacketSetPassengers(player));
                } else {
                    theEntity.setLeashedToEntity(player, true);
                    ((IAnimalTracked) theEntity).getData().setOwner(EntityHelper.getPlayerUUID(player));
                    player.worldObj.spawnEntityInWorld(theEntity);
                    player.connection.sendPacket(new SPacketEntityAttach(theEntity, theEntity.getLeashedToEntity()));
                }
            }
        }

        return false;
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}
package joshie.harvest.npc.entity;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.shops.ShopInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityNPCShopkeeper extends EntityNPC {
    private IShop shop;
    private boolean isWorking;

    public EntityNPCShopkeeper(UUID owning_player, EntityNPCShopkeeper entity) {
        super(owning_player, entity);
        shop = entity.shop;
        isWorking = entity.isWorking;
    }

    public EntityNPCShopkeeper(World world) {
        super(world);
    }

    public EntityNPCShopkeeper(UUID owning_player, World world, INPC npc) {
        super(owning_player, world, npc);
        shop = npc.getShop();
    }

    @Override
    protected void updateAITick() {
        if (!isWorking) {
            if (shop.isOpen(worldObj, null)) {
                isWorking = true;
                //setPosition(shopX, shopY, shopZ);
            }

            super.updateAITick();
        } else if (worldObj.getWorldTime() % 200 == 0 && !shop.isOpen(worldObj, null)) {
            isWorking = false;
        }
    }

    @Override
    public void setDead() {
        if (!worldObj.isRemote && npc.respawns()) {
            EntityNPCShopkeeper clone = new EntityNPCShopkeeper(owning_player, this);
            worldObj.spawnEntityInWorld(clone);
        }

        isDead = true;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        shop = npc.getShop();
        isWorking = nbt.getBoolean("IsWorking");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("IsWorking", isWorking);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        super.readSpawnData(buf);
        shop = npc.getShop();
    }
}

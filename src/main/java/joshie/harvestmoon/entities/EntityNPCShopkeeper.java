package joshie.harvestmoon.entities;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.shops.ShopInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityNPCShopkeeper extends EntityNPC {
    private ShopInventory shop;
    private boolean isWorking;
    private int shopX, shopY, shopZ;

    public EntityNPCShopkeeper(World world) {
        super(world);
    }

    public EntityNPCShopkeeper(World world, NPC npc) {
        super(world, npc);
        shop = npc.getShop();
    }

    public EntityNPCShopkeeper setWorkLocation(int x, int y, int z) {
        this.shopX = x;
        this.shopY = y;
        this.shopZ = z;
        return this;
    }

    @Override
    protected void updateAITick() {
        if (!isWorking) {
            if (shop.isOpen(worldObj)) {
                isWorking = true;
                setPosition(shopX, shopY, shopZ);
            }

            super.updateAITick();
        } else if (!shop.isOpen(worldObj)) {
            isWorking = false;
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        shop = npc.getShop();
        isWorking = nbt.getBoolean("IsWorking");
        shopX = nbt.getInteger("ShopX");
        shopY = nbt.getInteger("ShopY");
        shopZ = nbt.getInteger("ShopZ");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("IsWorking", isWorking);
        nbt.setInteger("ShopX", shopX);
        nbt.setInteger("ShopY", shopY);
        nbt.setInteger("ShopZ", shopZ);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        super.readSpawnData(buf);
        shop = npc.getShop();
    }
}

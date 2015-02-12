package joshie.harvestmoon.npc;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.shops.ShopInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityNPCShopkeeper extends EntityNPC {
    private ShopInventory shop;
    private boolean isWorking;
    private double shopX, shopY, shopZ;

    public EntityNPCShopkeeper(EntityNPCShopkeeper entity) {
        super(entity);
        shop = entity.shop;
        isWorking = entity.isWorking;
        shopX = entity.shopX;
        shopY = entity.shopY;
        shopZ = entity.shopZ;
    }

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
        shopX = nbt.getDouble("ShopX");
        shopY = nbt.getDouble("ShopY");
        shopZ = nbt.getDouble("ShopZ");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("IsWorking", isWorking);
        nbt.setDouble("ShopX", shopX);
        nbt.setDouble("ShopY", shopY);
        nbt.setDouble("ShopZ", shopZ);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        super.readSpawnData(buf);
        shop = npc.getShop();
    }
}

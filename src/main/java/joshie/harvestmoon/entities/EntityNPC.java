package joshie.harvestmoon.entities;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.lib.HMModInfo;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityNPC extends EntityVillager implements IEntityAdditionalSpawnData {
    public InventoryNPC inventory = new InventoryNPC(this);
    protected NPC npc;
    protected double homeX, homeY, homeZ;
    protected EntityNPC lover;

    public EntityNPC(EntityNPC entity) {
        this(entity.worldObj, entity.npc, entity.homeX, entity.homeY, entity.homeZ);
        inventory = entity.inventory;
        lover = entity.lover;
    }

    public EntityNPC(World world) {
        this(world, HMNPCs.goddess);
    }

    public EntityNPC(World world, NPC npc) {
        super(world);
        this.npc = npc;
    }

    public EntityNPC(World world, NPC npc, double x, double y, double z) {
        this(world, npc);
        setPosition(x, y, z);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);

        homeX = x;
        homeY = y;
        homeZ = z;
    }

    public ResourceLocation getSkin() {
        return new ResourceLocation(HMModInfo.MODPATH + ":" + "textures/entity/" + npc.getUnlocalizedName() + ".png");
    }

    public NPC getNPC() {
        return npc;
    }

    public EntityNPC getLover() {
        return lover;
    }

    @Override
    public String getCommandSenderName() {
        return npc.getLocalizedName();
    }

    @Override
    protected void updateEntityActionState() {
        if (!isTrading()) {
            super.updateEntityActionState();
        } else {
            addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
        }
    }

    @Override
    public void setDead() {
        if (!worldObj.isRemote) {
            EntityNPC clone = new EntityNPC(this);
            worldObj.spawnEntityInWorld(clone);
            for (int i = 0; i < 20; ++i) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                clone.worldObj.spawnParticle("explode", clone.posX + (double) (clone.rand.nextFloat() * clone.width * 2.0F) - (double) clone.width, clone.posY + (double) (clone.rand.nextFloat() * clone.height), clone.posZ + (double) (clone.rand.nextFloat() * clone.width * 2.0F) - (double) clone.width, d2, d0, d1);
            }
        }

        super.setDead();
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack held = player.inventory.getCurrentItem();
        boolean flag = held != null && held.getItem() == Items.spawn_egg;

        if (!flag && isEntityAlive() && !isTrading() && !isChild() && !player.isSneaking()) {
            if (!worldObj.isRemote) {
                player.openGui(HarvestMoon.instance, npc.getGuiID(worldObj), worldObj, getEntityId(), 0, 0);
                setCustomer(player);
            }

            return true;
        } else {
            return super.interact(player);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        inventory.readFromNBT(nbt.getTagList("Inventory", 10));
        npc = HMNPCs.get(nbt.getString("NPC"));
        homeX = nbt.getDouble("HomeX");
        homeY = nbt.getDouble("HomeY");
        homeZ = nbt.getDouble("HomeZ");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
        if (npc != null) {
            nbt.setString("NPC", npc.getUnlocalizedName());
        }

        nbt.setDouble("HomeX", homeX);
        nbt.setDouble("HomeY", homeY);
        nbt.setDouble("HomeZ", homeZ);
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        if (npc != null) {
            char[] name = npc.getUnlocalizedName().toCharArray();
            buf.writeByte(name.length);
            for (char c : name) {
                buf.writeChar(c);
            }
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        char[] name = new char[buf.readByte()];
        for (int i = 0; i < name.length; i++) {
            name[i] = buf.readChar();
        }

        npc = HMNPCs.get(new String(name));
        if (npc == null) {
            npc = HMNPCs.goddess;
        }
    }
}
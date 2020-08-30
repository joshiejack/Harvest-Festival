package joshie.harvest.fishing.entity;

import joshie.harvest.fishing.FishingHelper;
import joshie.harvest.fishing.item.ItemFish;
import joshie.harvest.fishing.item.ItemFishingRod;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemFishedEvent;

import java.util.Iterator;
import java.util.List;

public class EntityFishHookHF extends EntityFishHook {
    public EntityFishHookHF(World world) { this(world, null);}
    public EntityFishHookHF(World world, EntityPlayer player) {
        super(world, player);
    }

    @Override
    public boolean shouldStopFishing() {
        EntityPlayer angler = getAngler();
        ItemStack itemstack = angler.getHeldItemMainhand();
        ItemStack itemstack1 = angler.getHeldItemOffhand();
        //Changed by me, both flags
        boolean flag = itemstack.getItem() instanceof ItemFishingRod;
        boolean flag1 = itemstack1.getItem() instanceof ItemFishingRod;

        if (!angler.isDead && angler.isEntityAlive() && (flag || flag1) && getDistanceSq(angler) <= 1024.0D) {
            return false;
        } else {
            this.setDead();
            return true;
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public int handleHookRetraction() {
        if (!this.world.isRemote && getAngler() != null) {
            int i = 0;
            ItemFishedEvent event = null;
            if (this.caughtEntity != null) {
                this.bringInHookedEntity();
                this.world.setEntityState(this, (byte)31);
                i = this.caughtEntity instanceof EntityItem ? 3 : 5;
            } else if (this.ticksCatchable > 0) {
                Builder lootcontext$builder = new Builder((WorldServer)this.world);
                lootcontext$builder.withLuck((float)this.luck + this.getAngler().getLuck()).withPlayer(this.getAngler()).withLootedEntity(this);
                List<ItemStack> result = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.rand, lootcontext$builder.build());
                event = new ItemFishedEvent(result, this.inGround ? 2 : 1, this);
                MinecraftForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    this.setDead();
                    return event.getRodDamage();
                }

                Iterator var5 = result.iterator();

                label50:
                while(true) {
                    Item item;
                    do {
                        if (!var5.hasNext()) {
                            i = 1;
                            break label50;
                        }

                        ItemStack itemstack = (ItemStack)var5.next();
                        EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY, this.posZ, itemstack);
                        double d0 = getAngler().posX - this.posX;
                        double d1 = getAngler().posY - this.posY;
                        double d2 = getAngler().posZ - this.posZ;
                        double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        double d4 = 0.1D;
                        entityitem.motionX = d0 * 0.1D;
                        entityitem.motionY = d1 * 0.1D + (double)MathHelper.sqrt(d3) * 0.08D;
                        entityitem.motionZ = d2 * 0.1D;
                        this.world.spawnEntity(entityitem);
                        this.getAngler().world.spawnEntity(new EntityXPOrb(this.getAngler().world, this.getAngler().posX, this.getAngler().posY + 0.5D, this.getAngler().posZ + 0.5D, this.rand.nextInt(6) + 1));
                        item = itemstack.getItem();
                    } while(item != Items.FISH && item != Items.COOKED_FISH);

                    this.getAngler().addStat(StatList.FISH_CAUGHT, 1);
                }
            }

            if (this.inGround) {
                i = 2;
            }

            this.setDead();
            return event == null ? i : event.getRodDamage();
        } else {
            return 0;
        }
    }
}
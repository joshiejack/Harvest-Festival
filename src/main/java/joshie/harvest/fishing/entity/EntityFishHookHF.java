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
import net.minecraft.world.storage.loot.LootContext;

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

        if (!angler.isDead && angler.isEntityAlive() && (flag || flag1) && getDistanceSqToEntity(angler) <= 1024.0D) {
            return false;
        } else {
            this.setDead();
            return true;
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public int handleHookRetraction() {
        EntityPlayer angler = getAngler();
        if (!world.isRemote && angler != null) {
            int i = 0;
            net.minecraftforge.event.entity.player.ItemFishedEvent event = null;
            if (caughtEntity != null) {
                bringInHookedEntity();
                world.setEntityState(this, (byte) 31);
                i = caughtEntity instanceof EntityItem ? 3 : 5;
            } else if (ticksCatchable > 0) {
                LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
                lootcontext$builder.withLuck((float)this.field_191518_aw + getAngler().getLuck());
                //Line changed by me
                List<ItemStack> result = world.getLootTableManager().getLootTableFromLocation(FishingHelper.getFishingTable(world, new BlockPos(this))).generateLootForPools(rand, lootcontext$builder.build());
                event = new net.minecraftforge.event.entity.player.ItemFishedEvent(result, this.inGround ? 2 : 1, this);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    this.setDead();
                    return event.getRodDamage();
                }
                for (ItemStack stack : result) {
                    //Line changed by me
                    FishingHelper.track(stack, angler); //Add to the tracking
                    EntityItem entityItem = new EntityItem(world, posX, posY, posZ, stack);
                    double d0 = angler.posX - posX;
                    double d1 = angler.posY - posY;
                    double d2 = angler.posZ - posZ;
                    double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    entityItem.motionX = d0 * 0.1D;
                    entityItem.motionY = d1 * 0.1D + (double) MathHelper.sqrt(d3) * 0.08D;
                    entityItem.motionZ = d2 * 0.1D;
                    world.spawnEntity(entityItem);
                    angler.world.spawnEntity(new EntityXPOrb(angler.world, angler.posX, angler.posY + 0.5D, angler.posZ + 0.5D, rand.nextInt(6) + 1));

                    Item item = stack.getItem();
                    //Line changed by me
                    if (item instanceof ItemFish || item instanceof ItemFishFood || item == Items.FISH || item == Items.COOKED_FISH) {
                        angler.addStat(StatList.FISH_CAUGHT, 1);
                    }
                }
                i = 1;
            }

            if (inGround) {
                i = 2;
            }

            setDead();
            return event == null ? i : event.getRodDamage();
        } else {
            return 0;
        }
    }
}
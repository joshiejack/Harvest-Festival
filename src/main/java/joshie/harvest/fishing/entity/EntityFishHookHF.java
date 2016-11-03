package joshie.harvest.fishing.entity;

import joshie.harvest.fishing.FishingHelper;
import joshie.harvest.fishing.HFFishing;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

import java.util.List;

public class EntityFishHookHF extends EntityFishHook {
    @SuppressWarnings("ConstantConditions")
    public EntityFishHookHF(World world, EntityPlayer player) {
        super(world, player);
    }

    @SuppressWarnings("unused")
    public EntityFishHookHF(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        if (!worldObj.isRemote)  {
            setFlag(6, isGlowing());
        }

        onEntityUpdate();
        if (worldObj.isRemote) {
            int i = getDataManager().get(DATA_HOOKED_ENTITY);
            if (i > 0 && caughtEntity == null) {
                caughtEntity = worldObj.getEntityByID(i - 1);
            }
        } else {
            ItemStack itemstack = angler.getHeldItemMainhand();
            if (angler.isDead || !angler.isEntityAlive() || itemstack == null || itemstack.getItem() != HFFishing.FISHING_ROD || getDistanceSqToEntity(angler) > 1024.0D) {
                setDead();
                angler.fishEntity = null;
                return;
            }
        }

        if (caughtEntity != null) {
            if (!caughtEntity.isDead) {
                posX = caughtEntity.posX;
                double d17 = (double) caughtEntity.height;
                posY = caughtEntity.getEntityBoundingBox().minY + d17 * 0.8D;
                posZ = caughtEntity.posZ;
                return;
            }

            caughtEntity = null;
        }

        if (fishPosRotationIncrements > 0) {
            double d3 = this.posX + (fishX - posX) / (double) fishPosRotationIncrements;
            double d4 = posY + (fishY - posY) / (double) fishPosRotationIncrements;
            double d6 = posZ + (fishZ - posZ) / (double) fishPosRotationIncrements;
            double d8 = MathHelper.wrapDegrees(fishYaw - (double) rotationYaw);
            rotationYaw = (float) ((double) rotationYaw + d8 / (double) fishPosRotationIncrements);
            rotationPitch = (float) ((double) rotationPitch + (fishPitch - (double) rotationPitch) / (double) fishPosRotationIncrements);
            --fishPosRotationIncrements;
            setPosition(d3, d4, d6);
            setRotation(rotationYaw, rotationPitch);
        } else {
            if (inGround) {
                if (worldObj.getBlockState(field_189740_d).getBlock() == inTile) {
                    ++ticksInGround;
                    if (ticksInGround == 1200) {
                        setDead();
                    }

                    return;
                }

                inGround = false;
                motionX *= (double) (rand.nextFloat() * 0.2F);
                motionY *= (double) (rand.nextFloat() * 0.2F);
                motionZ *= (double) (rand.nextFloat() * 0.2F);
                ticksInGround = 0;
                ticksInAir = 0;
            } else {
                ++ticksInAir;
            }

            if (!worldObj.isRemote) {
                Vec3d vec3d1 = new Vec3d(posX, posY, posZ);
                Vec3d vec3d = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
                RayTraceResult raytraceresult = worldObj.rayTraceBlocks(vec3d1, vec3d);
                vec3d1 = new Vec3d(posX, posY, posZ);
                vec3d = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
                if (raytraceresult != null) {
                    vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
                }

                Entity entity = null;
                List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(motionX, motionY, motionZ).expandXyz(1.0D));
                double d0 = 0.0D;

                for (int j = 0; j < list.size(); ++j) {
                    Entity entity1 = list.get(j);
                    if (func_189739_a(entity1) && (entity1 != angler || ticksInAir >= 5)) {
                        AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
                        RayTraceResult raytraceresult1 = axisalignedbb1.calculateIntercept(vec3d1, vec3d);
                        if (raytraceresult1 != null) {
                            double d1 = vec3d1.squareDistanceTo(raytraceresult1.hitVec);
                            if (d1 < d0 || d0 == 0.0D) {
                                entity = entity1;
                                d0 = d1;
                            }
                        }
                    }
                }

                if (entity != null) {
                    raytraceresult = new RayTraceResult(entity);
                }

                if (raytraceresult != null) {
                    if (raytraceresult.entityHit != null) {
                        caughtEntity = raytraceresult.entityHit;
                        getDataManager().set(DATA_HOOKED_ENTITY, caughtEntity.getEntityId() + 1);
                    } else {
                        inGround = true;
                    }
                }
            }

            if (!inGround) {
                moveEntity(motionX, motionY, motionZ);
                float f2 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                rotationYaw = (float) (MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));

                for (rotationPitch = (float) (MathHelper.atan2(motionY, (double) f2) * (180D / Math.PI)); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F) {
                    ;
                }

                while (rotationPitch - prevRotationPitch >= 180.0F) {
                    prevRotationPitch += 360.0F;
                }

                while (rotationYaw - prevRotationYaw < -180.0F) {
                    prevRotationYaw -= 360.0F;
                }

                while (rotationYaw - prevRotationYaw >= 180.0F) {
                    prevRotationYaw += 360.0F;
                }

                rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
                rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
                float f3 = 0.92F;

                if (onGround || isCollidedHorizontally) {
                    f3 = 0.5F;
                }

                double d5 = 0.0D;
                for (int l = 0; l < 5; ++l) {
                    AxisAlignedBB axisalignedbb = getEntityBoundingBox();
                    double d9 = axisalignedbb.maxY - axisalignedbb.minY;
                    double d10 = axisalignedbb.minY + d9 * (double) l / 5.0D;
                    double d11 = axisalignedbb.minY + d9 * (double) (l + 1) / 5.0D;
                    AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(axisalignedbb.minX, d10, axisalignedbb.minZ, axisalignedbb.maxX, d11, axisalignedbb.maxZ);

                    if (worldObj.isAABBInMaterial(axisalignedbb2, Material.WATER)) {
                        d5 += 0.2D;
                    }
                }

                if (!worldObj.isRemote && d5 > 0.0D) {
                    WorldServer worldserver = (WorldServer) worldObj;
                    int i1 = 1;
                    BlockPos blockpos = (new BlockPos(this)).up();

                    if (rand.nextFloat() < 0.25F && worldObj.isRainingAt(blockpos)) {
                        i1 = 2;
                    }

                    if (rand.nextFloat() < 0.5F && !worldObj.canSeeSky(blockpos)) {
                        --i1;
                    }

                    if (ticksCatchable > 0) {
                        --ticksCatchable;

                        if (ticksCatchable <= 0) {
                            ticksCaughtDelay = 0;
                            ticksCatchableDelay = 0;
                        }
                    } else if (ticksCatchableDelay > 0) {
                        ticksCatchableDelay -= i1;

                        if (ticksCatchableDelay <= 0) {
                            motionY -= 0.20000000298023224D;
                            playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                            float f6 = (float) MathHelper.floor_double(getEntityBoundingBox().minY);
                            worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX, (double) (f6 + 1.0F), posZ, (int) (1.0F + width * 20.0F), (double) width, 0.0D, (double) width, 0.20000000298023224D, new int[0]);
                            worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, posX, (double) (f6 + 1.0F), posZ, (int) (1.0F + width * 20.0F), (double) width, 0.0D, (double) width, 0.20000000298023224D, new int[0]);
                            ticksCatchable = MathHelper.getRandomIntegerInRange(rand, 10, 30);
                        } else {
                            fishApproachAngle = (float) ((double) fishApproachAngle + rand.nextGaussian() * 4.0D);
                            float f5 = fishApproachAngle * 0.017453292F;
                            float f8 = MathHelper.sin(f5);
                            float f10 = MathHelper.cos(f5);
                            double d13 = posX + (double) (f8 * (float) ticksCatchableDelay * 0.1F);
                            double d15 = (double) ((float) MathHelper.floor_double(getEntityBoundingBox().minY) + 1.0F);
                            double d16 = posZ + (double) (f10 * (float) ticksCatchableDelay * 0.1F);
                            Block block1 = worldserver.getBlockState(new BlockPos((int) d13, (int) d15 - 1, (int) d16)).getBlock();
                            if (block1 == Blocks.WATER || block1 == Blocks.FLOWING_WATER) {
                                if (rand.nextFloat() < 0.15F) {
                                    worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d13, d15 - 0.10000000149011612D, d16, 1, (double) f8, 0.1D, (double) f10, 0.0D, new int[0]);
                                }

                                float f = f8 * 0.04F;
                                float f1 = f10 * 0.04F;
                                worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d13, d15, d16, 0, (double) f1, 0.01D, (double) (-f), 1.0D, new int[0]);
                                worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d13, d15, d16, 0, (double) (-f1), 0.01D, (double) f, 1.0D, new int[0]);
                            }
                        }
                    } else if (ticksCaughtDelay > 0) {
                        ticksCaughtDelay -= i1;
                        float f4 = 0.15F;
                        if (ticksCaughtDelay < 20) {
                            f4 = (float) ((double) f4 + (double) (20 - ticksCaughtDelay) * 0.05D);
                        } else if (ticksCaughtDelay < 40) {
                            f4 = (float) ((double) f4 + (double) (40 - ticksCaughtDelay) * 0.02D);
                        } else if (ticksCaughtDelay < 60) {
                            f4 = (float) ((double) f4 + (double) (60 - ticksCaughtDelay) * 0.01D);
                        }

                        if (rand.nextFloat() < f4) {
                            float f7 = MathHelper.randomFloatClamp(rand, 0.0F, 360.0F) * 0.017453292F;
                            float f9 = MathHelper.randomFloatClamp(rand, 25.0F, 60.0F);
                            double d12 = posX + (double) (MathHelper.sin(f7) * f9 * 0.1F);
                            double d14 = (double) ((float) MathHelper.floor_double(getEntityBoundingBox().minY) + 1.0F);
                            double d2 = posZ + (double) (MathHelper.cos(f7) * f9 * 0.1F);
                            Block block = worldserver.getBlockState(new BlockPos((int) d12, (int) d14 - 1, (int) d2)).getBlock();
                            if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
                                worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d12, d14, d2, 2 + rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
                            }
                        }

                        if (ticksCaughtDelay <= 0) {
                            fishApproachAngle = MathHelper.randomFloatClamp(rand, 0.0F, 360.0F);
                            ticksCatchableDelay = MathHelper.getRandomIntegerInRange(rand, 20, 80);
                        }
                    } else {
                        ticksCaughtDelay = MathHelper.getRandomIntegerInRange(rand, 100, 900);
                        ticksCaughtDelay -= EnchantmentHelper.getLureModifier(angler) * 20 * 5;
                    }

                    if (ticksCatchable > 0) {
                        motionY -= (double) (rand.nextFloat() * rand.nextFloat() * rand.nextFloat()) * 0.2D;
                    }
                }

                double d7 = d5 * 2.0D - 1.0D;
                motionY += 0.03999999910593033D * d7;

                if (d5 > 0.0D) {
                    f3 = (float) ((double) f3 * 0.9D);
                    motionY *= 0.8D;
                }

                motionX *= (double) f3;
                motionY *= (double) f3;
                motionZ *= (double) f3;
                setPosition(posX, posY, posZ);
            }
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public int handleHookRetraction() {
        if (worldObj.isRemote) {
            return 0;
        } else {
            int i = 0;

            if (caughtEntity != null) {
                bringInHookedEntity();
                worldObj.setEntityState(this, (byte) 31);
                i = caughtEntity instanceof EntityItem ? 3 : 5;
            } else if (ticksCatchable > 0) {
                LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) worldObj);
                lootcontext$builder.withLuck((float) EnchantmentHelper.getLuckOfSeaModifier(angler) + angler.getLuck());
                lootcontext$builder.withLootedEntity(this);
                lootcontext$builder.withPlayer(angler);
                for (ItemStack itemstack : worldObj.getLootTableManager().getLootTableFromLocation(FishingHelper.getFishingTable(worldObj, new BlockPos(this))).generateLootForPools(rand, lootcontext$builder.build())) {
                    EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, itemstack);
                    double d0 = angler.posX - posX;
                    double d1 = angler.posY - posY;
                    double d2 = angler.posZ - posZ;
                    double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
                    entityitem.motionX = d0 * 0.1D;
                    entityitem.motionY = d1 * 0.1D + (double) MathHelper.sqrt_double(d3) * 0.08D;
                    entityitem.motionZ = d2 * 0.1D;
                    worldObj.spawnEntityInWorld(entityitem);
                    angler.worldObj.spawnEntityInWorld(new EntityXPOrb(angler.worldObj, angler.posX, angler.posY + 0.5D, angler.posZ + 0.5D, rand.nextInt(6) + 1));
                }

                i = 1;
            }

            if (inGround) {
                i = 2;
            }

            setDead();
            angler.fishEntity = null;
            return i;
        }
    }
}


package uk.joshiejack.energy;

import uk.joshiejack.energy.events.AddExhaustionEvent;
import uk.joshiejack.energy.events.AddFoodStatsEvent;
import uk.joshiejack.energy.events.EnergyUsageEvents;
import uk.joshiejack.energy.packet.PacketSyncStats;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;

public class EnergyFoodStats extends net.minecraft.util.FoodStats {
    private static final float ENERGY_PER_HUNGER = 16F;
    public int maxFoodDisplay = EnergyConfig.startingEnergy;
    public double maxHearts = EnergyConfig.startingHealth;
    private EntityPlayer player;

    public EnergyFoodStats(EntityPlayer player) {
        this.player = player;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
        this.foodLevel = Math.min(foodLevel, maxFoodDisplay);
    }

    public static float getEnergyRequiredForSprint(FoodStats stats) {
        if (stats instanceof EnergyFoodStats) {
            return (((float) ((EnergyFoodStats)stats).maxFoodDisplay) / 10) * 3.0F;
        } else return 6.0F;
    }

    public boolean increaseMaxHealth() {
        if (maxHearts < 20) {
            maxHearts += 2D;
            if (!player.world.isRemote) {
                PenguinNetwork.sendToClient(new PacketSyncStats(this), player);
                player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHearts);
            }

            return true;
        }

        return false;
    }

    public boolean increaseMaxEnergy() {
        if (maxFoodDisplay < 24) {
            maxFoodDisplay+= 2;
            if (!player.world.isRemote) {
                PenguinNetwork.sendToClient(new PacketSyncStats(this), player);
            }

            return true;
        }

        return false;
    }

    @Override
    public void addStats(ItemFood foodItem, @Nonnull ItemStack stack)  {
        AddFoodStatsEvent event = new AddFoodStatsEvent(player, stack, foodItem.getHealAmount(stack), foodItem.getSaturationModifier(stack));
        MinecraftForge.EVENT_BUS.post(event);
        addStats(event.getNewHealth(), event.getNewSaturation());
    }

    @Override
    public void addStats(int foodLevelIn, float foodSaturationModifier) {
        boolean healing = player.shouldHeal();
        float health = player.getHealth();
        if (healing) player.heal(foodLevelIn * (1F + foodSaturationModifier));
        float newHealth = player.getHealth();

        float totalRestore = ((foodLevelIn * ENERGY_PER_HUNGER) * foodSaturationModifier) - (newHealth - health);
        if (totalRestore > 0) {
            foodLevel = (int) (Math.min(foodLevel + 1 + (totalRestore / ENERGY_PER_HUNGER), maxFoodDisplay));
            foodSaturationLevel = Math.min(ENERGY_PER_HUNGER, foodSaturationLevel + totalRestore % ENERGY_PER_HUNGER);
            EnergyUsageEvents.updateFatigue(player, false);
        }
    }

    @Override
    public void onUpdate(EntityPlayer player) {} //No natural loss

    @Override
    public void addExhaustion(float exhaustion) {
        AddExhaustionEvent event = new AddExhaustionEvent(player, exhaustion);
        MinecraftForge.EVENT_BUS.post(event);
        exhaustion = event.getNewValue();

        boolean outside = player.world.isRainingAt(new BlockPos(player));
        float modifier = outside ? (player.world.isThundering() ? 2 : 1) : 0; //Extra loss if it's raining or even more if it's a storm
        this.foodSaturationLevel = Math.min(ENERGY_PER_HUNGER, Math.max(0F, this.foodSaturationLevel - exhaustion - modifier));
        if (foodLevel > maxFoodDisplay) foodLevel = maxFoodDisplay;
        if (this.foodLevel > 0 && this.foodSaturationLevel <= 0F) {
            this.foodSaturationLevel = ENERGY_PER_HUNGER;
            this.foodLevel--;
        }

        EnergyUsageEvents.updatePotionEffects(player);
    }

    @Override
    public boolean needFood() {
        return foodLevel < maxFoodDisplay || player.getHealth() < maxHearts;
    }

    @Override
    public void readNBT(NBTTagCompound compound) {
        super.readNBT(compound);
        if (compound.hasKey("maxFoodDisplay")) {
            maxFoodDisplay = compound.getByte("maxFoodDisplay");
        }

        if (compound.hasKey("maxHearts")) {
            maxHearts = compound.getDouble("maxHearts");
        }
    }

    @Override
    public void writeNBT(NBTTagCompound compound) {
        super.writeNBT(compound);
        compound.setByte("maxFoodDisplay", (byte) maxFoodDisplay);
        compound.setDouble("maxHearts", maxHearts);
    }
}

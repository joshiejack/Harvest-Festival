package uk.joshiejack.energy.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.energy.Energy;
import uk.joshiejack.energy.EnergyEffects;
import uk.joshiejack.penguinlib.item.base.ItemBaseHammer;
import uk.joshiejack.penguinlib.item.base.ItemBaseSickle;
import uk.joshiejack.penguinlib.item.base.ItemBaseWateringCan;

@SuppressWarnings("WeakerAccess, unused, ConstantConditions")
@Mod.EventBusSubscriber(modid = Energy.MODID)
public class EnergyUsageEvents {
    private static final float LOW = 0.75F;
    private static final float MEDIUM = 1.25F;
    private static final float HIGH = 2.5F;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        ItemStack held = player.getHeldItemMainhand();
        if (held.getItem() instanceof ItemBaseHammer || held.getItem() instanceof ItemSpade || held.getItem() instanceof ItemAxe || held.getItem() instanceof ItemBaseSickle) {
            player.getFoodStats().addExhaustion(LOW);
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.PlaceEvent event) {
        ItemStack held = event.getPlayer().getHeldItem(event.getHand());
        if (held.getItem() instanceof ItemHoe || held.getItem() instanceof ItemBaseWateringCan) {
            consumeEnergy(event.getPlayer(), LOW);
        }
    }

    @SubscribeEvent
    public static void onMultiPlaced(BlockEvent.MultiPlaceEvent event) {
        ItemStack held = event.getPlayer().getHeldItem(event.getHand());
        if (held.getItem() instanceof ItemHoe || held.getItem() instanceof ItemBaseWateringCan) {
            for (int i = 0; i < event.getReplacedBlockSnapshots().size(); i++) consumeEnergy(event.getPlayer(), LOW);
        }
    }

    @SubscribeEvent
    public static void onFishing(ItemFishedEvent event) {
        consumeEnergy(event.getEntityPlayer(), HIGH);
    }

    private static void consumeEnergy(EntityPlayer player, float amount) {
        if (!player.capabilities.isCreativeMode) {
            player.getFoodStats().addExhaustion(amount);
        }
    }

    public static void updateTiredness(EntityPlayer player) {
        if (player.capabilities.isCreativeMode && player.isPotionActive(EnergyEffects.TIRED)) {
            player.removePotionEffect(EnergyEffects.TIRED);
        } else {
            //Add the last slept tag
            if (!player.getEntityData().hasKey("LastSlept")) {
                player.getEntityData().setLong("LastSlept", player.world.getWorldTime());
            }

            long slept = player.getEntityData().getLong("LastSlept"); //If we've been awake for over 16 hours, make us tired
            if (player.world.getWorldTime() - slept > 16000L && !player.isPotionActive(EnergyEffects.BUZZED)) {
                player.addPotionEffect(new PotionEffect(EnergyEffects.TIRED, Integer.MAX_VALUE)); //Unlimited
            } else if (player.isPotionActive(EnergyEffects.TIRED)) player.removePotionEffect(EnergyEffects.TIRED);
        }
    }

    public static void updateFatigue(EntityPlayer player, boolean expiring) {
        boolean fatigued = player.isPotionActive(EnergyEffects.FATIGUE);
        boolean exhausted = player.isPotionActive(EnergyEffects.EXHAUSTION);
        if (!player.isPotionActive(EnergyEffects.BUZZED)) {
            int food = player.getFoodStats().foodLevel;
            if (food <= 2) {
                if (!fatigued && !exhausted && food > 0) player.addPotionEffect(new PotionEffect(EnergyEffects.FATIGUE, 600));
                if ((expiring && food <= 1) || (!fatigued && food <= 0)) player.addPotionEffect(new PotionEffect(EnergyEffects.EXHAUSTION, Integer.MAX_VALUE));
            } else if (fatigued) {
                player.removePotionEffect(EnergyEffects.FATIGUE);
            } else if (exhausted) {
                player.removePotionEffect(EnergyEffects.EXHAUSTION);
            }
        } else {
            if (fatigued) player.removePotionEffect(EnergyEffects.FATIGUE);
            if (exhausted) player.removePotionEffect(EnergyEffects.EXHAUSTION);
        }
    }

    public static void updatePotionEffects(EntityPlayer player) {
        if (player.isCreative()) {
            player.removePotionEffect(EnergyEffects.TIRED);
            player.removePotionEffect(EnergyEffects.FATIGUE);
            player.removePotionEffect(EnergyEffects.EXHAUSTION);
        } else {
            updateTiredness(player);
            updateFatigue(player, false);
        }
    }
}

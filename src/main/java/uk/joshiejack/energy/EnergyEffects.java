package uk.joshiejack.energy;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.energy.events.EnergyUsageEvents;
import uk.joshiejack.penguinlib.potion.PenguinPotion;

import javax.annotation.Nonnull;
import java.util.List;

import static net.minecraft.entity.SharedMonsterAttributes.*;

@GameRegistry.ObjectHolder(Energy.MODID)
@Mod.EventBusSubscriber(modid = Energy.MODID)
public class EnergyEffects {
    public static final Potion TIRED = null;
    public static final Potion FATIGUE = null;
    public static final Potion EXHAUSTION = null;
    public static final Potion BUZZED = null;

    @SubscribeEvent
    public static void registerPotions(final RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(
                new EnergyPotion("tired", 0x666666, 1, 0).registerPotionAttributeModifier(ATTACK_DAMAGE, "8107BC5D-5CF8-4030-440C-314C1E160890", -0.20000000596046448D, 2)
                        .registerPotionAttributeModifier(ATTACK_SPEED, "8107BC5D-5CF8-4030-440C-314C1E160891", -0.20000000596046448D, 2),

                new EnergyPotion("fatigue", 0xD9D900, 0, 0) {
                    @Override
                    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
                        if (expiring && entity instanceof EntityPlayer)
                            EnergyUsageEvents.updateFatigue((EntityPlayer) entity, true);
                        else super.performEffect(entity, amplifier);
                    }
                }       .registerPotionAttributeModifier(MOVEMENT_SPEED, "8107BC5E-7CF8-4030-440C-514C1F160890", -0.10000000596046448D, 2)
                        .registerPotionAttributeModifier(ATTACK_SPEED, "8107BC5D-5CF8-4030-440C-314C1E165831", -0.15000000596046448D, 2),

                new EnergyPotion("exhaustion", 0xD9D900, 3, 0)
                        .registerPotionAttributeModifier(MOVEMENT_SPEED, "8107BC5E-7CF4-4030-440C-514C1F160890", -0.50000000596046448D, 2)
                        .registerPotionAttributeModifier(ATTACK_SPEED, "8107BC5D-5CF8-4030-440C-314C1E160892", -0.50000000596046448D, 2),

                new EnergyPotion("buzzed", 0xA5A29C, 4, 0) {
                    @Override
                    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
                        if (expiring && entity instanceof EntityPlayer)
                            EnergyUsageEvents.updateFatigue((EntityPlayer) entity, true);
                        else super.performEffect(entity, amplifier);
                    }
                }.registerPotionAttributeModifier(ATTACK_SPEED, "FB353E1C-4181-4865-B01B-BCCE9785ACA3", 0.10000000149011612D, 2)
                        .registerPotionAttributeModifier(MOVEMENT_SPEED, "8107BD55-7CF8-4030-441C-514C1F160890", 0.20000000298023224D, 2)
                        .setBeneficial()
        );
    }

    public static class EnergyPotion extends PenguinPotion {
        EnergyPotion(String name, int color, int x, int y) {
            super(new ResourceLocation(Energy.MODID, name), color, x, y);
        }

        boolean expiring = false;

        @Override
        public boolean isReady(int duration, int amplifier) {
            expiring = duration == 1;
            return expiring || super.isReady(duration, amplifier);
        }

        @Nonnull
        @Override
        public List<ItemStack> getCurativeItems() {
            return Lists.newArrayList();
        }
    }
}

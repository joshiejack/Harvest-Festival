package uk.joshiejack.husbandry.database;

import com.google.common.collect.Lists;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.animals.traits.AnimalTrait;
import uk.joshiejack.husbandry.entity.EntityDuck;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;

@Mod.EventBusSubscriber(modid = Husbandry.MODID)
public class HusbandryTraitLoader {
    @SubscribeEvent
    public static void onCollectForRegistration(CollectRegistryEvent event) {
        event.add(AnimalTrait.class, (d, c, s, l) -> AnimalTrait.TRAITS.put(l, c.getConstructor(String.class).newInstance(l)));
    }

    @SubscribeEvent
    public static void registerDuck(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(EntityDuck.class)
                .id(new ResourceLocation(Husbandry.MODID, "duck"), 0)
                .name("duck")
                .tracker(80, 3, true)
                .egg(0x096913, 0xEADCDC)
                .spawn(EnumCreatureType.CREATURE, 80, 2, 3, Lists.newArrayList(Biomes.RIVER))
                .build());
    }
}

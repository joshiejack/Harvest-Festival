package uk.joshiejack.husbandry.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.animals.AnimalProducts;
import uk.joshiejack.husbandry.animals.AnimalType;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.animals.traits.AnimalTrait;
import uk.joshiejack.husbandry.network.PacketRequestData;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Husbandry.MODID)
public class AnimalDataLoader {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        Map<String, AnimalProducts> products = Maps.newHashMap();
        products.put("none", AnimalProducts.NONE);
        event.table("animal_products").rows().forEach(product -> {
            String name = product.get("name");
            List<ItemStack> stacks = products(product);
            if (stacks.size() == 3) {
                products.put(name, new AnimalProducts(product.get("days between producing"), stacks.get(0), stacks.get(1), stacks.get(2)));
            } else products.put(name, AnimalProducts.NONE); //If we failed to load
        });

        Map<String, AnimalType> types = Maps.newHashMap();
        event.table("animals").rows().forEach(animal_type -> {
            String name = animal_type.get("name");
            List<AnimalTrait> traits = Lists.newArrayList();
            event.table("animal_traits").where("animal=" + name)
                    .forEach(trait -> traits.add(AnimalTrait.TRAITS.get(trait.get("trait").toString())));

            AnimalType type = new AnimalType(animal_type.get("min age"), animal_type.get("max age"),
                    StackHelper.getStackFromString("treat item"),
                    animal_type.get("generic treats"), animal_type.get("type treats")
                    , animal_type.get("days to birth"), animal_type.get("days to maturity"),
                    products.get(animal_type.get("products").toString()), traits);
            types.put(name, type);
            Husbandry.logger.log(Level.INFO, "Registered a new animal type: " + name);
        });

        event.table("animal_entities").rows().forEach(entities -> {
            String name = entities.get("animal");
            ResourceLocation resource = new ResourceLocation(entities.get("entity"));
            Class<? extends Entity> clazz = EntityList.getClass(resource);
            if (clazz != null) {
                AnimalType.TYPES.put(resource, types.get(name));
                Husbandry.logger.log(Level.INFO, "Registered the entity " + entities.get("entity") + " as a " + name);
            }
        });
    }

    private static List<ItemStack> products(Row row) {
        List<ItemStack> list = Lists.newArrayList();
        String[] suffix = new String[]{"small", "medium", "large"};
        for (String s : suffix) {
            ItemStack stack = StackHelper.getStackFromString(row.get(s));
            if (!stack.isEmpty()) {
                list.add(stack);
            } else return list;
        }

        return list;
    }

    @SubscribeEvent
    public static void onCapabilityLoading(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        ResourceLocation entityResource = EntityList.getKey(entity);
        if (entity instanceof EntityAgeable && AnimalType.TYPES.containsKey(entityResource)) {
            event.addCapability(new ResourceLocation(Husbandry.MODID, "stats"),  new AnimalStats<>((EntityAgeable) entity, AnimalType.TYPES.get(entityResource)));
        }
    }

    @SubscribeEvent
    public static void onEntityJoinedWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            //Request data about this animal from the server
            PenguinNetwork.sendToServer(new PacketRequestData(event.getEntity().getEntityId()));
        }
    }
}

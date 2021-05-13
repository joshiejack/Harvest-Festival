package uk.joshiejack.harvestcore.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.mine.dimension.floors.*;
import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import uk.joshiejack.harvestcore.world.mine.tier.TierBuilder;
import uk.joshiejack.harvestcore.world.storage.loot.conditions.mine.*;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class MineLoader {
    public static final Map<ResourceLocation, Tier> TIERS = Maps.newHashMap();
    private static final Map<String, RoomGenerator> GENERATORS = Maps.newHashMap();
    public static RoomGenerator CIRCLE;

    @SubscribeEvent
    public static void preDatabase(DatabaseLoadedEvent.Pre event) {
        LootConditionManager.registerCondition(new ConditionFloorBetween.Serializer());
        LootConditionManager.registerCondition(new ConditionFloorBetweenScaled.Serializer());
        LootConditionManager.registerCondition(new ConditionFloorFrom.Serializer());
        LootConditionManager.registerCondition(new ConditionFloorMatch.Serializer());
        LootConditionManager.registerCondition(new ConditionFloorMultipleOf.Serializer());
        CIRCLE = newGenerator("harvestcore:circle", new RoomGeneratorCircle());
        newGenerator("harvestcore:tunnel", new RoomGeneratorTunnel());
        newGenerator("harvestcore:tunnel_multi", new RoomGeneratorTunnelMulti());
        newGenerator("harvestcore:sprawl", new RoomGeneratorTunnelSprawl());
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("mine_rooms").rows().forEach(row -> {
            ResourceLocation texture = new ResourceLocation(row.get("texture"));
            try {
                newGenerator(texture.toString(), new RoomGeneratorTemplate(new ResourceLocation(texture.getNamespace(), "custom/rooms/" + texture.getPath() + ".png")));
            } catch (IOException e) { e.printStackTrace(); }
        });

        event.table("mines").rows().forEach(row -> {
            String name = row.get("name");
            String nameID = row.get("nameid");
            int dimension = row.get("dimensionid");
            String exit = row.get("exitpoint");
            Mine.create(name, nameID, exit, dimension, event.table("mine_tiers")
                    .where("mineid="+nameID).stream()
                    .map(r -> r.get("tierid").toString())
                    .collect(Collectors.toList())); //Create the new mines
        });

        MineLoader.loadTiers(); //Init the tiers
    }

    public static void loadTiers() {
        //.create(HFConfig.mineID);
        List<TierBuilder> tiers = Lists.newArrayList();
        //Load the blocks and then the items
        tiers.addAll(ResourceLoader.loadJson(TierBuilder.class, "tiers"));
        Collections.sort(tiers); //Sort in priority order
        tiers.forEach(tier -> TIERS.put(tier.registryName, tier.build())); //Build the tiers
        Mine.BY_ID.values().forEach(Mine::init);
        //TIERS.values().forEach(default_::addTier); //Add them all to the default dimension
    }

    public static RoomGenerator newGenerator(String name, RoomGenerator generator) {
        GENERATORS.put(name, generator);
        return generator;
    }

    public static RoomGenerator getRoom(String name) {
        return GENERATORS.get(name);
    }
}

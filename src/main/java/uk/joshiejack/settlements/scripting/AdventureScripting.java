package uk.joshiejack.settlements.scripting;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.client.scripting.wrappers.TownClientJS;
import uk.joshiejack.settlements.client.town.TownClient;
import uk.joshiejack.settlements.command.CommandNPCGenerator;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.settlements.quest.data.QuestTracker;
import uk.joshiejack.settlements.scripting.wrappers.*;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.settlements.world.town.land.TownBuilding;
import uk.joshiejack.penguinlib.events.CollectScriptingWrappers;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingMethods;
import uk.joshiejack.penguinlib.scripting.wrappers.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

import static uk.joshiejack.settlements.Settlements.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class AdventureScripting {
    @SubscribeEvent
    public static void onCollectWrappers(CollectScriptingWrappers event) {
        event.registerExtensible(EntityNPCJS.class, EntityNPC.class).setDynamic().setSided();
        event.register(NPCStatusJS.class, ResourceLocation.class).disable();
        event.register(NPCTaskJS.class, NPCTaskJS.TaskList.class);
        event.register(QuestTrackerJS.class, QuestTracker.class);
        event.register(QuestJS.class, Quest.class);
        event.register(TownBuildingJS.class, TownBuilding.class);
        event.register(TownClientJS.class, TownClient.class);
        event.register(TownServerJS.class, TownServer.class);
    }

    @SubscribeEvent
    public static void onCollectScriptingMethods(CollectScriptingMethods event) {
        event.add("display");
        event.add("onRightClickedNPC");
        event.add("onNPCSpawned");
    }

    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("settlements", AdventureScripting.class);
    }

    public static void createChild(PlayerJS playerJS) {
        CommandNPCGenerator.createEntityWithClassAndName(playerJS.penguinScriptingObject, "child", null);
    }

    public static ItemStackJS building(String building) {
        return WrapperRegistry.wrap(AdventureItems.BUILDING.getStackFromResource(new ResourceLocation(building)));
    }

    public static ItemStackJS blueprint(String blueprint) {
        return WrapperRegistry.wrap(AdventureItems.BLUEPRINT.getStackFromResource(new ResourceLocation(blueprint)));
    }

    public static ItemStackJS getNPCIcon(String npc) {
        return WrapperRegistry.wrap(AdventureItems.NPC_SPAWNER.getStackFromResource(new ResourceLocation(npc)));
    }

    public static EntityNPCJS getNearbyNPC(PlayerJS player) {
        return getNearbyNPC(player, null);
    }

    public static EntityNPCJS getNearbyNPC(PlayerJS player, String name) {
        return getNPC((WorldServerJS) player.world(), name, player.pos().x(), player.pos().y(), player.pos().z(), 64);
    }

    public static EntityNPCJS getOrSpawnNPCAt(PlayerJS playerW, PositionJS posW, String npc) {
        EntityNPCJS entity = getNearbyNPC(playerW, npc);
        if (entity == null) entity = ((TownServerJS)TownScripting.find((WorldServerJS) playerW.world(), posW)).spawn_at(playerW.world(), posW, npc);
        return entity; //Return the entity
    }

    public static EntityNPCJS getNPC(WorldServerJS world, @Nullable String name, int x, int y, int z, int distance) {
        ResourceLocation npc = name == null ? NPC.NULL_NPC.getRegistryName() : new ResourceLocation(name);
        List<EntityNPC> entity = world.penguinScriptingObject.getEntitiesWithinAABB(EntityNPC.class, new AxisAlignedBB(x - 0.5F, y - 0.5F, z - 0.5F, x + 0.5F, y + 0.5F, z + 0.5F).expand(distance, distance, distance));
        for (EntityNPC e: entity) {
            if (npc.equals(NPC.NULL_NPC.getRegistryName()) || e.getInfo().getRegistryName().equals(npc)) return WrapperRegistry.wrap(e);
        }

        return null;
    }

    private static NPCStatusJS[] statuses;

    public static NPCStatusJS[] statuses(WorldJS<?> world) {
        if (statuses == null) {
            List<NPCStatusJS> list = Lists.newArrayList();
            NPC.all().forEach(n -> list.add(WrapperRegistry.wrap(n.getRegistryName())));
            TownFinder.all((World) world.penguinScriptingObject)
                    .forEach(t -> t.getCensus().getCustomNPCKeys().forEach(k -> list.add(WrapperRegistry.wrap(k))));
            statuses = list.toArray(new NPCStatusJS[0]);
        }

        return statuses;
    }

    public static NPCStatusJS status(String name) {
        return WrapperRegistry.wrap(new ResourceLocation(name));
    }

    public static QuestTrackerJS quests(WorldJS<?> worldJS) {
        return new QuestTrackerJS(AdventureDataLoader.get(worldJS.penguinScriptingObject).getServerTracker());
    }

    public static QuestTrackerJS quests(PlayerJS player) {
        return new QuestTrackerJS(AdventureDataLoader.get(player.penguinScriptingObject.world).getPlayerTracker(player.penguinScriptingObject));
    }

    public static QuestTrackerJS quests(WorldJS<?> worldJS, TeamJS team) {
        return new QuestTrackerJS(AdventureDataLoader.get(worldJS.penguinScriptingObject).getTeamTracker(team.penguinScriptingObject.getID()));
    }
}

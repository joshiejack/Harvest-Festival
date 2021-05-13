package uk.joshiejack.penguinlib.scripting;

import com.google.common.collect.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.BlockSmashedEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingMethods;
import uk.joshiejack.penguinlib.scripting.event.ScriptingTriggerFired;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MOD_ID)
public class Scripting {
    private static final Map<ResourceLocation, Interpreter> REGISTRY = Maps.newHashMap();
    private static final Multimap<String, Interpreter> METHOD_TO_SCRIPTS = HashMultimap.create();
    private static final List<String> METHODS = Lists.newArrayList();

    @Deprecated
    public static Interpreter get(ResourceLocation resource) {
        return REGISTRY.get(resource);
    }

    public static Interpreter getScript(ResourceLocation resource) {
        return get(resource);
    }

    public static boolean scriptExists(ResourceLocation resource) {
        return REGISTRY.containsKey(resource);
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.Pre event) {
        //Load in all the javascript files
        File directory = new File(PenguinLib.getDirectory(), "scripts");
        if (!directory.exists()) directory.mkdir();
        FileUtils.listFiles(directory, new String[] { "js" }, true).forEach(file -> {
            ResourceLocation registryName = new ResourceLocation(PenguinLib.MOD_ID, file.getName().replace(".js", ""));
            Scripting.register(file, registryName); //REGISTER YOU
        }); //Load in from the configs

        ResourceLoader.get().getResourceListInDirectory("scripts", "js", Sets.newHashSet("includes")).forEach(scriptLocation -> {
            ResourceLocation registryName = new ResourceLocation(scriptLocation.getNamespace(), scriptLocation.getPath().replace("/", "_"));
            register(scriptLocation, registryName);
            PenguinLib.logger.log(Level.INFO, "Registered the Script: " + registryName + " with the script at " + scriptLocation);
        });
    }

    @SubscribeEvent
    public static void onCollectScriptingMethods(CollectScriptingMethods event) {
        event.add("onEntityInteract");
        event.add("onEntityKilled");
        event.add("onItemFished");
        event.add("onUseHoe");
        event.add("onPlayerLogin");
        event.add("onRightClickBlock");
        event.add("onGetBreakSpeed");
        event.add("onBlockSmashed");
        event.add("onHarvestDrop");
        event.add("onItemExpire");
    }

    public static Set<ResourceLocation> IGNORE = Sets.newHashSet();

    @SubscribeEvent
    public static void onTriggerFired(ScriptingTriggerFired event) {
        METHOD_TO_SCRIPTS.get(event.getMethod()).stream().filter(it -> !IGNORE.contains(it.this_id)).forEach(script -> script.callFunction(event.getMethod(), event.getObjects()));
        IGNORE.clear(); //Clear out the ignore list
    }

    @SubscribeEvent
    public static void onPlayerJoinedWorld(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerJS player = WrapperRegistry.wrap(event.player);
        long birthday = player.spawnday();
        if (birthday == -1) {
            player.setBirthday(event.player.world.getWorldTime()); //Mark the players birthday
        }

        MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onPlayerLogin", event.player));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemExpire(ItemExpireEvent event) {
        try {
            if (!event.isCanceled()) {
                double original = 0;
                int extra = 0;
                for (Interpreter interpreter : METHOD_TO_SCRIPTS.get("onItemExpire")) {
                    Object ret = interpreter.getResultOfFunction("onItemExpire", event.getEntityItem(), extra);
                    if (ret instanceof Integer) {
                        extra = ((Integer)ret);
                    }
                }

                if (extra != 0) {
                    event.setCanceled(true);
                    event.setExtraLife(extra);
                }
            }
        } catch (NullPointerException ignored) {}
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityInteract(PlayerInteractEvent.RightClickBlock event) {
        MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onRightClickBlock", event.getEntityPlayer(), event.getPos(), event.getItemStack()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityLivingBase)
            MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onEntityInteract", event.getEntityPlayer(), event.getTarget(), event.getHand()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityDeath(LivingDeathEvent event) {
        EntityPlayer player = PlayerHelper.getPlayerFromSource(event.getSource());
        if (player != null && (event.getEntity() instanceof EntityLivingBase)) {
            MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onEntityKilled", player, event.getEntity()));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemFished(ItemFishedEvent event) {
        if (!event.getDrops().isEmpty())
            MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onItemFished", event.getEntityPlayer(), event.getDrops().get(0), event.getHookEntity()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onUseHoe(UseHoeEvent event) {
        if (!event.isCanceled())
            MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onUseHoe", event.getEntityPlayer(), event.getPos()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreakSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
        if (!event.isCanceled()) {
            float original = event.getOriginalSpeed();
            float newValue = original;
            for (Interpreter interpreter: METHOD_TO_SCRIPTS.get("onGetBreakSpeed")) {
                Object ret = interpreter.getResultOfFunction("onGetBreakSpeed", event.getEntityPlayer(), event.getState(), event.getPos(), newValue);
                if (ret != null && !ret.toString().equalsIgnoreCase("null")) {
                    newValue = Float.parseFloat(ret.toString());
                }
            }

            if (original != newValue) event.setNewSpeed(newValue);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockSmashed(BlockSmashedEvent event) {
        boolean canceled = false;
        for (Interpreter interpreter: METHOD_TO_SCRIPTS.get("onBlockSmashed")) {
            if ((Boolean) interpreter.getResultOfFunction("onBlockSmashed", event.getEntityPlayer(), event.getHand(), event.getPos(), event.getState())) {
                canceled = true;
            }
        }

        if (canceled) event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHarvestDrop(BlockEvent.HarvestDropsEvent event) {
        MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onHarvestDrop", event));
    }

    public static void register(Object scriptLocation, ResourceLocation registryName) {
        Interpreter interpreter = new Interpreter(scriptLocation).init(registryName);
        getMethods().forEach(method -> {
            if (interpreter.hasMethod(method))
                METHOD_TO_SCRIPTS.get(method).add(interpreter);
        });

        REGISTRY.put(registryName, interpreter);
    }

    public static List<String> getMethods() {
        if (METHODS.size() == 0) {
            MinecraftForge.EVENT_BUS.post(new CollectScriptingMethods(METHODS));
        }

        return METHODS;
    }

    public static void callFunction(@Nullable ResourceLocation script, String function, Object... data) {
        if (script != null) callFunction(REGISTRY.get(script), function, data);
    }

    public static void callFunction(@Nullable Interpreter script, String function, Object... data) {
        if (script != null) script.callFunction(function, data);
    }

    @Nonnull
    public static <R> R getResult(@Nullable ResourceLocation script, String function, R default_, Object... data) {
        return script == null ? default_ : getResult(REGISTRY.get(script), function, default_, data);
    }

    @Nonnull
    public static <R> R getResult(@Nullable Interpreter script, String function, R default_, Object... data) {
        return script == null ? default_ : script.getValue(function, default_, data);
    }

    public static boolean functionExists(ResourceLocation script, String function) {
        return functionExists(REGISTRY.get(script), function);
    }

    public static boolean functionExists(@Nullable Interpreter script, String function) {
        return script != null && script.hasMethod(function);
    }

    public static void reload() {
        new ArrayList<>(Scripting.REGISTRY.keySet())
                .forEach(key -> Scripting.register(Scripting.getScript(key).getLocation(), key));
    }
}

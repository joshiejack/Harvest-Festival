package joshie.harvest.core;

import com.google.common.collect.Lists;
import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.HFRegister;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.commands.HFCommandBase;
import joshie.harvest.core.handlers.ShippingRegistry;
import joshie.harvest.core.handlers.SizeableRegistry;
import joshie.harvest.core.network.Packet;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.gathering.GatheringRegistry;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.player.PlayerAPI;
import joshie.harvest.shops.ShopRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static joshie.harvest.core.network.PacketHandler.registerPacket;

public class HFApiLoader {
    public static void init() {
        //Register API Handlers
        HFApi.animals = new AnimalRegistry();
        HFApi.buildings = new BuildingRegistry();
        HFApi.calendar = new CalendarAPI();
        HFApi.crops = new CropRegistry();
        HFApi.cooking = new FoodRegistry();
        HFApi.gathering = new GatheringRegistry();
        HFApi.npc = new NPCRegistry();
        HFApi.player = new PlayerAPI();
        HFApi.shops = new ShopRegistry();
        HFApi.shipping = new ShippingRegistry();
        HFApi.sizeable = new SizeableRegistry();
        HFApi.tickable = new HFDailyTickable();
    }

    public static <I extends Impl> void load(Class type, FMLControlledNamespacedRegistry registry, ResourceLocation resource, Class<I> clazz) {
        try {
            if (type.isAssignableFrom(clazz)) {
                registry.register(clazz.newInstance().setRegistryName(resource));
            }
        } catch (Exception e) {}
    }

    public static void load(@Nonnull ASMDataTable asm) {
        String annotationClassName = HFRegister.class.getCanonicalName();
        Set<ASMData> asmDatas = new HashSet<>(asm.getAll(annotationClassName));
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Map<String, Object> data = asmData.getAnnotationInfo();
                Class clazz = Class.forName(asmData.getClassName());
                String extra = (String) data.get("data");
                if (HFCommandBase.class.isAssignableFrom(clazz)) {
                    CommandManager.INSTANCE.registerCommand((HFCommandBase) clazz.newInstance());
                } else if (extra.equals("events")) {
                    Method register = getMethod(clazz, "register");
                    if (register == null || ((Boolean)register.invoke(null))) {
                        Field INSTANCE = getField(clazz, "INSTANCE");
                        if (INSTANCE == null) MinecraftForge.EVENT_BUS.register(clazz.newInstance());
                        else MinecraftForge.EVENT_BUS.register(INSTANCE.get(null));
                    }
                } else {
                    String domain = data.get("mod") != null ? (String) data.get("mod") : "harvestfestival";
                    ResourceLocation resource = new ResourceLocation(domain, extra);
                    load(Quest.class, Quest.REGISTRY, resource, clazz);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        //Load in Packets
        registerPackets(asm);
    }

    private static void registerPackets(@Nonnull ASMDataTable asmDataTable) {
        String annotationClassName = Packet.class.getCanonicalName();
        Set<ASMData> asmDatas = new HashSet<>(asmDataTable.getAll(annotationClassName));

        HashMap<String, Pair<Side, Class>> sidedPackets = new HashMap();
        HashMap<String, Class> unsidedPackets = new HashMap();
        topLoop:
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());
                Map<String, Object> data = asmData.getAnnotationInfo();
                boolean isSided = data.get("isSided") != null ? (Boolean) data.get("isSided") : false;
                if (isSided) {
                    String s = ReflectionHelper.getPrivateValue(ModAnnotation.EnumHolder.class, (ModAnnotation.EnumHolder) data.get("side"), "value");
                    Side side = s.equals("CLIENT") ? Side.CLIENT : Side.SERVER;
                    sidedPackets.put(asmClass.getSimpleName(), Pair.of(side, (Class)asmClass));
                } else unsidedPackets.put(asmClass.getSimpleName(), asmClass);
            } catch (Exception e) { e.printStackTrace(); }
        }


        //Sort the packet alphabetically so they get registered the same on server and client side
        Comparator<String> alphabetical = new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (res == 0) {
                    res = str1.compareTo(str2);
                }

                return res;
            }
        };

        //Sort the sided and unsided packets
        List<String> namesSided = Lists.newArrayList(sidedPackets.keySet());
        Collections.sort(namesSided, alphabetical);
        List<String> namesUnsided = Lists.newArrayList(unsidedPackets.keySet());
        Collections.sort(namesUnsided, alphabetical);

        //Register sided packets
        for (String sided: namesSided) {
            Pair<Side, Class> result = sidedPackets.get(sided);
            try {
                registerPacket(result.getRight(), result.getLeft());
            } catch (Exception e) { e.printStackTrace(); }
        }

        //Register unsided packets
        for (String unsided: namesUnsided) {
            try {
                registerPacket(unsidedPackets.get(unsided));
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private static Method getMethod(Class clazz, String method) {
        try {
            return clazz.getMethod(method);
        } catch (NoSuchMethodException e) { return null; }
    }

    private static Field getField(Class clazz, String field) {
        try {
            return clazz.getField(field);
        } catch (NoSuchFieldException e) { return null; }
    }
}

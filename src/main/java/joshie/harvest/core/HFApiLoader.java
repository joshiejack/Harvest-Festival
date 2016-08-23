package joshie.harvest.core;

import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.HFCommand;
import joshie.harvest.api.HFRegister;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.core.util.HFEvents;
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
    public static void init(@Nonnull ASMDataTable table) {
        Set<ASMData> datas = new HashSet<>(table.getAll(HFApiImplementation.class.getCanonicalName()));
        for (ASMDataTable.ASMData data : datas) {
            try {
                Class clazz = Class.forName(data.getClassName());
                Object instance = clazz.getField("INSTANCE").get(null);
                Class[] interfaces = clazz.getInterfaces();
                if (interfaces != null && interfaces.length > 0) {
                    for (Class inter: interfaces) {
                        for (Field f: HFApi.class.getFields()) {
                            if (f.getType().equals(inter)) {
                                f.set(null, instance);
                            }
                        }
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @SuppressWarnings("unchecked")
    private static <I extends Impl> void load(Class type, FMLControlledNamespacedRegistry registry, ResourceLocation resource, Class<I> clazz) {
        try {
            if (type.isAssignableFrom(clazz)) {
                registry.register(clazz.newInstance().setRegistryName(resource));
            }
        } catch (Exception e) {}
    }

    public static void load(@Nonnull ASMDataTable asm, boolean isClient) {
        registerEverything(asm);
        registerEvents(asm, isClient);
        registerPackets(asm);
    }

    private static void registerEverything(@Nonnull ASMDataTable asm) {
        String annotationClassName = HFRegister.class.getCanonicalName();
        Set<ASMData> asmDatas = new HashSet<>(asm.getAll(annotationClassName));
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Map<String, Object> data = asmData.getAnnotationInfo();
                Class clazz = Class.forName(asmData.getClassName());
                String extra = data.get("data") != null ? (String) data.get("data") : "";
                if (HFCommand.class.isAssignableFrom(clazz)) {
                    CommandManager.INSTANCE.registerCommand((HFCommand) clazz.newInstance());
                } else {
                    String domain = data.get("mod") != null ? (String) data.get("mod") : "harvestfestival";
                    ResourceLocation resource = new ResourceLocation(domain, extra);
                    load(Quest.class, Quest.REGISTRY, resource, clazz);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private static void registerEvents(@Nonnull ASMDataTable asmDataTable, boolean isClient) {
        String annotationClassName = HFEvents.class.getCanonicalName();
        Set<ASMData> asmDatas = new HashSet<>(asmDataTable.getAll(annotationClassName));
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Map<String, Object> data = asmData.getAnnotationInfo();
                String side = data.get("value") != null ? ReflectionHelper.getPrivateValue(ModAnnotation.EnumHolder.class, (ModAnnotation.EnumHolder) data.get("value"), "value") : "";
                if ((side.equals("CLIENT") && isClient) || side.equals("")) {
                    Class clazz = Class.forName(asmData.getClassName());
                    Method register = getMethod(clazz, "register");
                    if (register == null || ((Boolean)register.invoke(null))) {
                        Field INSTANCE = getField(clazz, "INSTANCE");
                        if (INSTANCE == null) MinecraftForge.EVENT_BUS.register(clazz.newInstance());
                        else MinecraftForge.EVENT_BUS.register(INSTANCE.get(null));
                    }
                }
            } catch (Exception e) {}
        }
    }

    @SuppressWarnings("unchecked")
    private static void registerPackets(@Nonnull ASMDataTable asmDataTable) {
        String annotationClassName = Packet.class.getCanonicalName();
        Set<ASMData> asmDatas = new HashSet<>(asmDataTable.getAll(annotationClassName));

        HashMap<String, Pair<Side, Class>> sidedPackets = new HashMap();
        HashMap<String, Class> unsidedPackets = new HashMap();
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());
                Map<String, Object> data = asmData.getAnnotationInfo();
                String s = data.get("value") != null ? ReflectionHelper.getPrivateValue(ModAnnotation.EnumHolder.class, (ModAnnotation.EnumHolder) data.get("value"), "value") : "BOTH";
                Side side = s.equals("CLIENT") ? Side.CLIENT : s.equals("SERVER") ? Side.SERVER: null;
                if (side == null) unsidedPackets.put(asmClass.getSimpleName(), asmClass);
                else sidedPackets.put(asmClass.getSimpleName(), Pair.of(side, (Class)asmClass));
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

    @SuppressWarnings("unchecked")
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

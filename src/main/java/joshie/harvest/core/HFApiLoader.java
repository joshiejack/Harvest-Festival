package joshie.harvest.core;

import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.HFRegister;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.core.handlers.ShippingRegistry;
import joshie.harvest.core.handlers.SizeableRegistry;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.gathering.GatheringRegistry;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.player.PlayerAPI;
import joshie.harvest.shops.ShopRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
                if (extra.equals("events")) {
                    Method register = clazz.getMethod("register");
                    if (register == null || ((Boolean)register.invoke(null))) {
                        MinecraftForge.EVENT_BUS.register(clazz.newInstance());
                    }

                } else {
                    String domain = data.get("mod") != null ? (String) data.get("mod") : "harvestfestival";
                    ResourceLocation resource = new ResourceLocation(domain, extra);
                    load(Quest.class, Quest.REGISTRY, resource, clazz);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}

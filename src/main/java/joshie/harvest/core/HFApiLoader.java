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
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;

import javax.annotation.Nonnull;
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
                String domain = data.get("domain") != null ? (String) data.get("domain"): "harvestfestival";
                String path = (String) data.get("path");
                ResourceLocation resource = new ResourceLocation(domain, path);
                Class clazz = Class.forName(asmData.getClassName());
                load(Quest.class, Quest.REGISTRY, resource, clazz);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}

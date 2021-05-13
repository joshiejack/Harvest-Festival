package uk.joshiejack.penguinlib;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class PenguinCommon {
    public static final EnumSet<Side> DEFAULT = EnumSet.allOf(Side.class);

    protected boolean isClient() {
        return false;
    }

    //private Map<String, Class<?extends TileEntity>> tiles = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public void setup(ASMDataTable table) {
        List<ASMDataTable.ASMData> datas = Lists.newArrayList(table.getAll(PenguinLoader.class.getCanonicalName()));
        Map<Class, PenguinRegister> runnableMap = Maps.newHashMap();
        MinecraftForge.EVENT_BUS.post(new CollectRegistryEvent(runnableMap));
        datas.sort(Comparator.comparing(ASMDataTable.ASMData::getClassName)); //Order the data
        datas.forEach(data -> setupGlobal(data, runnableMap)); //Do everything that is needed first
        datas.forEach(data -> setupClient(data, runnableMap));//Now reload where neccessary
    }

    @SuppressWarnings("unchecked")
    public static EnumSet<Side> getSidesFromData(Map<String, Object> map) {
        List<ModAnnotation.EnumHolder> sidesEnum = (List<ModAnnotation.EnumHolder>) map.get("side");
        EnumSet<Side> sides = DEFAULT;
        if (sidesEnum != null) {
            sides = EnumSet.noneOf(Side.class);
            for (ModAnnotation.EnumHolder h : sidesEnum) {
                sides.add(Side.valueOf(h.getValue()));
            }
        }

        return sides;
    }

    public static Map<Class, Runnable> map = Maps.newLinkedHashMap();

    public void onConstruction() {}

    public interface PenguinRegister<T> {
        void register(ASMDataTable.ASMData d, Class<T> c, EnumSet<Side> s, String l) throws IllegalAccessException, InstantiationException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException;
    }

    @SuppressWarnings("unchecked")
    private void setupGlobal(ASMDataTable.ASMData data, Map<Class, PenguinRegister> runnableMap) {
        try {
            Map<String, Object> map = data.getAnnotationInfo();
            EnumSet<Side> sides = getSidesFromData(map);
            String loadingData = map.get("value") != null ? (String) map.get("value") : "";
            Class clazz = Class.forName(data.getClassName());
            runnableMap.forEach((k, v) -> {
                if (k.isAssignableFrom(clazz)) {
                    try {
                        v.register(data, clazz, sides, loadingData);
                    } catch (IllegalAccessException | InstantiationException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (ClassNotFoundException e) {
            //e.printStackTrace(); //Commented out to prevent a lot of errors on server launch
        }
    }

    public void setupClient(ASMDataTable.ASMData data, Map<Class, PenguinRegister> map) {
    }
}
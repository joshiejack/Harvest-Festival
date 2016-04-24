package joshie.harvest.asm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joshie.harvest.asm.transformers.*;
import joshie.harvest.core.config.HFConfig;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HFOverride implements IFMLLoadingPlugin, IClassTransformer {
    private static final int EGG = 0;
    private static final int SNOW = 1;
    private static final int FARMLAND = 2;

    public static boolean isObfuscated = false;
    private static List<AbstractASM> asm = new ArrayList();
    static {
        asm.add(new EggTransformer());
        asm.add(new SeedFoodTransformer());
        asm.add(new WheatTransformer());
        asm.add(new MelonTransformer());
        asm.add(new FarmlandHardnessTransformer());
        asm.add(new SnowTransformer());
        asm.add(new FarmlandTransformer());
        asm.add(new PumpkinTransformer());
        asm.add(new WeatherTransformer());
        //TODO: Future Plugins
        //transformers.add(new PamTransformer());

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
        Gson gson = builder.create();
        HFConfig.initASM(gson);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        byte[] modified = data;
        for (AbstractASM a : asm) {
            if (a.isActive(HFConfig.asm)) {
                if (a.isClass(name)) {
                    if (a.isVisitor()) {
                        ClassReader cr = new ClassReader(modified);
                        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        ClassVisitor cv = a.newInstance(cw);
                        cr.accept(cv, ClassReader.EXPAND_FRAMES);
                        modified = cw.toByteArray();
                    }

                    modified = a.transform(modified);
                }
            }
        }

        return modified;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { HFOverride.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        HFOverride.isObfuscated = ((Boolean) data.get("runtimeDeobfuscationEnabled"));
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}

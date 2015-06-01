package joshie.harvest.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import joshie.harvest.asm.transformers.EggTransformer;
import joshie.harvest.asm.transformers.FarmlandHardnessTransformer;
import joshie.harvest.asm.transformers.FarmlandTransformer;
import joshie.harvest.asm.transformers.ITransformer;
import joshie.harvest.asm.transformers.MelonTransformer;
import joshie.harvest.asm.transformers.PumpkinTransformer;
import joshie.harvest.asm.transformers.SeedFoodTransformer;
import joshie.harvest.asm.transformers.SnowTransformer;
import joshie.harvest.asm.transformers.WheatTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class HFOverride implements IFMLLoadingPlugin, IClassTransformer {
    private static final int EGG = 0;
    private static final int SNOW = 1;
    private static final int FARMLAND = 2;

    public static boolean isObfuscated = false;
    private static List<ITransformer> transformers = new ArrayList();
    static {
        transformers.add(new EggTransformer());
        transformers.add(new SeedFoodTransformer());
        transformers.add(new WheatTransformer());
        transformers.add(new MelonTransformer());
        transformers.add(new FarmlandHardnessTransformer());
        transformers.add(new SnowTransformer());
        transformers.add(new FarmlandTransformer());
        transformers.add(new PumpkinTransformer());
        //TODO: Future Plugins
        //transformers.add(new PamTransformer());

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
        Gson gson = builder.create();

        HFConfig.initASM(gson);
        for (int i = 0; i < transformers.size(); i++) {
            if (!transformers.get(i).isActive(HFConfig.vanilla)) {
                transformers.remove(i);
            }
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        byte[] modified = data;
        for (ITransformer t : transformers) {
            if (name.equals(t.getClass(isObfuscated))) {
                modified = t.transform(modified, isObfuscated);
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

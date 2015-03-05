package joshie.harvestmoon.init;

import java.util.ArrayList;
import java.util.List;

import joshie.harvestmoon.asm.transformers.EggTransformer;
import joshie.harvestmoon.asm.transformers.FarmlandHardnessTransformer;
import joshie.harvestmoon.asm.transformers.FarmlandTransformer;
import joshie.harvestmoon.asm.transformers.ITransformer;
import joshie.harvestmoon.asm.transformers.SeedFoodTransformer;
import joshie.harvestmoon.asm.transformers.SnowTransformer;
import joshie.harvestmoon.asm.transformers.WheatTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HMOverride implements IClassTransformer {
    private static final int EGG = 0;
    private static final int SNOW = 1;
    private static final int FARMLAND = 2;

    public static boolean isObfuscated = false;
    private static List<ITransformer> transformers = new ArrayList();
    static {
        transformers.add(new EggTransformer());
        transformers.add(new SeedFoodTransformer());
        transformers.add(new WheatTransformer());
        transformers.add(new FarmlandHardnessTransformer());
        transformers.add(new SnowTransformer());
        transformers.add(new FarmlandTransformer());
        //TODO: Future Plugins
        //transformers.add(new PamTransformer());

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
        Gson gson = builder.create();

        HMConfiguration.initASM(gson);
        for (int i = 0; i < transformers.size(); i++) {
            if (!transformers.get(i).isActive(HMConfiguration.vanilla)) {
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
}

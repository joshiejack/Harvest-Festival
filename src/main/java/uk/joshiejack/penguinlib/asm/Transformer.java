package uk.joshiejack.penguinlib.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class Transformer implements IFMLLoadingPlugin, IClassTransformer {
    private static final List<AbstractASM> ASM = new ArrayList<>();
    private List<String> transformers = new ArrayList<>();

    public Transformer() { //Constructor without parameters needed to get IFMLLoadingPlugin to load
        registerASMTransformer("penguinlib", "uk.joshiejack.penguinlib.asm.PenguinTransformer");
        registerASMTransformer("energy", "uk.joshiejack.energy.asm.EnergyTransformer");
        //registerASMTransformer("harvestcore", "uk.joshiejack.harvestcore.tweaks.BeetrootFix");
        registerASMTransformer("seasons", "uk.joshiejack.seasons.asm.SeasonsTransformer");
    }

    public Transformer(AbstractASM... asms) {
        Collections.addAll(ASM, asms);
    }

    @Override
    public String[] getASMTransformerClass() {
        return transformers.toArray(new String[0]);
    }

    private void registerASMTransformer(String modPackage, String classPath) {
        if (getClass().getClassLoader().getResource("uk/joshiejack/" + modPackage) != null) {
            transformers.add(classPath);
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        byte[] modified = data;
        for (AbstractASM a : ASM) {
            if (a.isClass(name)) {
                modified = a.transform(modified);
            }
        }

        return modified;
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
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
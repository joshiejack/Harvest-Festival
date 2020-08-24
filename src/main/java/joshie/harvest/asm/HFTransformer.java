package joshie.harvest.asm;

import joshie.harvest.asm.transformers.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.11.2")
public class HFTransformer implements IFMLLoadingPlugin, IClassTransformer {
    public static boolean isObfuscated = false;
    private static final List<AbstractASM> asm = new ArrayList<>();

    static {
        asm.add(new PlayerWakeTransformer());
        asm.add(new RenderRainTransformer());
        asm.add(new RenderItemTransformer());
        // asm.add(new FishingTransformer(4, "net.minecraft.entity.projectile.EntityFishHook", "zc"));
        asm.add(new FishingTransformer(3, "com.teammetallurgy.aquaculture.handlers.EntityCustomFishHook"));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        byte[] modified = data;
        for (AbstractASM a : asm) {
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

        return modified;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{ HFTransformer.class.getName() };
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
        HFTransformer.isObfuscated = ((Boolean) data.get("runtimeDeobfuscationEnabled"));
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
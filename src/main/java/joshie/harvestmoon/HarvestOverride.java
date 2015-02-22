package joshie.harvestmoon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import joshie.harvestmoon.asm.EggTransformer;
import joshie.harvestmoon.asm.ITransformer;
import joshie.harvestmoon.asm.SeedFoodTransformer;
import joshie.harvestmoon.config.Overrides;
import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HarvestOverride implements IClassTransformer {
    private static final int EGG = 0;
    private static final int SNOW = 1;
    private static final int FARMLAND = 2;
    
    static boolean isObfuscated = false;
    private static List<ITransformer> transformers = new ArrayList();
    static {
        transformers.add(new EggTransformer());
        transformers.add(new SeedFoodTransformer());

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();

        File file = new File("config/hm/override.json");
        if (!file.exists()) {
            try {
                HMConfiguration.overrides = new Overrides();
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(gson.toJson(HMConfiguration.overrides));
                writer.close(); //Write the default json to file
            } catch (Exception e) {}
        } else {
            try {
                HMConfiguration.overrides = gson.fromJson(FileUtils.readFileToString(file), Overrides.class);
            } catch (Exception e) {}
        }

        for (int i = 0; i < transformers.size(); i++) {
            if (!transformers.get(i).isActive(HMConfiguration.overrides)) {
                transformers.remove(i);
            }
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        for (ITransformer t : transformers) {
            if (name.equals(t.getClass(isObfuscated))) {
                return t.transform(data, isObfuscated);
            }
        }

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        boolean[] overridden = new boolean[3];
        String registerItems = !name.equals(transformedName) ? "l" : "registerItems";
        if (name.equals("net.minecraft.item.Item") || name.equals("net.minecraft.block.Block")) {
            for (MethodNode method : classNode.methods) {
                if (!HMConfiguration.overrides.egg && method.name.equals("registerItems") && method.desc.equals("()V")) {
                    for (int j = 0; j < method.instructions.size(); j++) {
                        AbstractInsnNode instruction = method.instructions.get(j);
                        if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                            LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                            if (ldcInstruction.cst.equals("egg")) {
                                if (!overridden[EGG]) {
                                    ((TypeInsnNode) method.instructions.get(j + 1)).desc = "joshie/harvestmoon/items/ItemSizedEgg";
                                    ((MethodInsnNode) method.instructions.get(j + 3)).owner = "joshie/harvestmoon/items/ItemSizedEgg";
                                    ((MethodInsnNode) method.instructions.get(j + 5)).owner = "joshie/harvestmoon/items/ItemSizedEgg";
                                    overridden[EGG] = true;
                                }
                            }
                        }
                    }
                } else if (method.name.equals("registerBlocks") && method.desc.equals("()V")) {
                    for (int j = 0; j < method.instructions.size(); j++) {
                        AbstractInsnNode instruction = method.instructions.get(j);
                        if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                            LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                            if (HMConfiguration.overrides.snow && ldcInstruction.cst.equals("snow_layer")) {
                                if (!overridden[SNOW]) {
                                    ((TypeInsnNode) method.instructions.get(j + 1)).desc = "joshie/harvestmoon/blocks/BlockSnowSheet";
                                    ((MethodInsnNode) method.instructions.get(j + 3)).owner = "joshie/harvestmoon/blocks/BlockSnowSheet";
                                    ((MethodInsnNode) method.instructions.get(j + 5)).owner = "joshie/harvestmoon/blocks/BlockSnowSheet";
                                    overridden[SNOW] = true;

                                }
                            } else if (HMConfiguration.overrides.farmland && ldcInstruction.cst.equals("wheat")) {
                                if (!overridden[FARMLAND]) {
                                    ((TypeInsnNode) method.instructions.get(j + 11)).desc = "joshie/harvestmoon/blocks/BlockSoil";
                                    ((MethodInsnNode) method.instructions.get(j + 13)).owner = "joshie/harvestmoon/blocks/BlockSoil";
                                    ((MethodInsnNode) method.instructions.get(j + 15)).owner = "joshie/harvestmoon/blocks/BlockSoil";
                                    overridden[FARMLAND] = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}

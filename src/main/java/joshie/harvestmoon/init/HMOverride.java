package joshie.harvestmoon.init;

import static joshie.harvestmoon.init.HMConfiguration.vanilla;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import joshie.harvestmoon.asm.EggTransformer;
import joshie.harvestmoon.asm.ITransformer;
import joshie.harvestmoon.asm.SeedFoodTransformer;
import joshie.harvestmoon.asm.WheatTransformer;
import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.config.Vanilla;
import joshie.harvestmoon.core.lib.HMModInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
        Gson gson = builder.create();

        File file = new File("config/" + HMModInfo.MODPATH + "/vanilla.json");
        if (!file.exists()) {
            try {
                HMConfiguration.vanilla = new Vanilla();
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(gson.toJson(HMConfiguration.vanilla));
                writer.close(); //Write the default json to file
            } catch (Exception e) {}
        } else {
            try {
                HMConfiguration.vanilla = gson.fromJson(FileUtils.readFileToString(file), Vanilla.class);
            } catch (Exception e) {}
        }

        for (int i = 0; i < transformers.size(); i++) {
            if (!transformers.get(i).isActive(HMConfiguration.vanilla)) {
                transformers.remove(i);
            }
        }
    }

    public static void init() {
        if (HMConfiguration.vanilla.HOES_HIDDEN) {
            Items.wooden_hoe.setCreativeTab(null);
            Items.stone_hoe.setCreativeTab(null);
            Items.iron_hoe.setCreativeTab(null);
            Items.diamond_hoe.setCreativeTab(null);
            Items.golden_hoe.setCreativeTab(null);
        }

        if (vanilla.CARROT_BLOCK_DISABLE_TICKING) Blocks.carrots.setTickRandomly(false);
        if (vanilla.POTATO_BLOCK_DISABLE_TICKING) Blocks.potatoes.setTickRandomly(false);
        if (vanilla.WHEAT_BLOCK_DISABLE_TICKING) Blocks.wheat.setTickRandomly(false);
        if (vanilla.MOVE_OVERRIDE_TAB) {
            if (vanilla.CARROT_OVERRIDE) Items.carrot.setCreativeTab(HMTab.tabGeneral);
            if (vanilla.POTATO_OVERRIDE) Items.potato.setCreativeTab(HMTab.tabGeneral);
            if (vanilla.WHEAT_OVERRIDE) Items.wheat.setCreativeTab(HMTab.tabGeneral);
            if (vanilla.EGG_OVERRIDE) Items.egg.setCreativeTab(HMTab.tabGeneral);
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
                if (method.name.equals("registerBlocks") && method.desc.equals("()V")) {
                    for (int j = 0; j < method.instructions.size(); j++) {
                        AbstractInsnNode instruction = method.instructions.get(j);
                        if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                            LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                            if (HMConfiguration.vanilla.SNOW_OVERRIDE && ldcInstruction.cst.equals("snow_layer")) {
                                if (!overridden[SNOW]) {
                                    ((TypeInsnNode) method.instructions.get(j + 1)).desc = HMModInfo.ASMPATH + "blocks/BlockSnowSheet";
                                    ((MethodInsnNode) method.instructions.get(j + 3)).owner = HMModInfo.ASMPATH + "blocks/BlockSnowSheet";
                                    ((MethodInsnNode) method.instructions.get(j + 5)).owner = HMModInfo.ASMPATH + "blocks/BlockSnowSheet";
                                    overridden[SNOW] = true;

                                }
                            } else if (HMConfiguration.vanilla.FARMLAND_OVERRIDE && ldcInstruction.cst.equals("wheat")) {
                                if (!overridden[FARMLAND]) {
                                    ((TypeInsnNode) method.instructions.get(j + 11)).desc = HMModInfo.ASMPATH + "blocks/BlockSoil";
                                    ((MethodInsnNode) method.instructions.get(j + 13)).owner = HMModInfo.ASMPATH + "blocks/BlockSoil";
                                    ((MethodInsnNode) method.instructions.get(j + 15)).owner = HMModInfo.ASMPATH + "blocks/BlockSoil";
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

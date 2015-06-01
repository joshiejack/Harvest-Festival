package joshie.harvest.asm.transformers;

import java.util.Iterator;

import joshie.harvest.core.config.Vanilla;
import joshie.harvest.core.lib.HFModInfo;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class MelonTransformer implements ITransformer {
    @Override
    public boolean isActive(Vanilla config) {
        return config.WATERMELON_OVERRIDE;
    }

    @Override
    public String getClass(boolean isObfuscated) {
        return isObfuscated ? "adb" : "net.minecraft.item.Item";
    }

    @Override
    public byte[] transform(byte[] data, boolean isObfuscated) {
        byte[] modified = data;
        String name = isObfuscated ? "func_150900_l" : "registerItems";
        String desc = "()V";

        ClassNode node = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(node, 0);

        //Remove all Instructions from the onItemUse Method
        Iterator<MethodNode> methods = node.methods.iterator();
        labelTop: while (methods.hasNext()) {
            MethodNode m = methods.next();
            if ((m.name.equals(name) && m.desc.equals(desc))) {
                for (int j = 0; j < m.instructions.size(); j++) {
                    AbstractInsnNode instruction = m.instructions.get(j);
                    if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                        LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                        if (ldcInstruction.cst.equals("melon")) {
                            ((TypeInsnNode) m.instructions.get(j + 1)).desc = HFModInfo.ASMPATH + "asm/overrides/ItemMelon";
                            ((MethodInsnNode) m.instructions.get(j + 6)).owner = HFModInfo.ASMPATH + "asm/overrides/ItemMelon";
                            ((MethodInsnNode) m.instructions.get(j + 8)).desc = "(Ljava/lang/String;)L" + HFModInfo.ASMPATH + "asm/overrides/ItemMelon;";
                            ((MethodInsnNode) m.instructions.get(j + 8)).owner = HFModInfo.ASMPATH + "asm/overrides/ItemMelon";
                            ((MethodInsnNode) m.instructions.get(j + 10)).desc = "(Ljava/lang/String;)L" + HFModInfo.ASMPATH + "asm/overrides/ItemMelon;";
                            ((MethodInsnNode) m.instructions.get(j + 10)).owner = HFModInfo.ASMPATH + "asm/overrides/ItemMelon";
                            break labelTop;
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}

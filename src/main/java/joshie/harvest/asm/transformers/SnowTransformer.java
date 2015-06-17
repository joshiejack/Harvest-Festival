package joshie.harvest.asm.transformers;

import joshie.harvest.core.config.ASM;
import joshie.harvest.core.lib.HFModInfo;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class SnowTransformer extends AbstractASM {
    @Override
    public boolean isActive(ASM config) {
        return config.SNOW_OVERRIDE;
    }

    @Override
    public boolean isClass(String name) {
        return name.equals("aji") || name.equals("net.minecraft.block.Block");
    }
    
    @Override
    public boolean isVisitor() {
        return false;
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        topLabel: for (MethodNode method : classNode.methods) {
            if ((method.name.equals("registerBlocks") || method.name.equals("func_149671_p")) && method.desc.equals("()V")) {
                for (int j = 0; j < method.instructions.size(); j++) {
                    AbstractInsnNode instruction = method.instructions.get(j);
                    if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                        LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                        if (ldcInstruction.cst.equals("snow_layer")) {
                            ((TypeInsnNode) method.instructions.get(j + 1)).desc = HFModInfo.ASMPATH + "asm/overrides/BlockSnowSheet";
                            ((MethodInsnNode) method.instructions.get(j + 3)).owner = HFModInfo.ASMPATH + "asm/overrides/BlockSnowSheet";
                            ((MethodInsnNode) method.instructions.get(j + 5)).owner = HFModInfo.ASMPATH + "asm/overrides/BlockSnowSheet";
                            break topLabel;
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

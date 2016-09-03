package joshie.harvest.asm.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

public class BlockSnowTransformer extends AbstractASM {
    private static final String SNOW = "joshie/harvest/asm/BlockSnow";

    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.block.Block") || name.equals("afk");
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

        topLabel:
        for (MethodNode method : classNode.methods) {
            if ((method.name.equals("registerBlocks") || method.name.equals("func_149671_p") || method.name.equals("x")) && method.desc.equals("()V")) {
                for (int j = 0; j < method.instructions.size(); j++) {
                    AbstractInsnNode instruction = method.instructions.get(j);
                    if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                        LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                        if (ldcInstruction.cst.equals("snow_layer")) {
                            ((TypeInsnNode) method.instructions.get(j + 1)).desc = SNOW;
                            ((MethodInsnNode) method.instructions.get(j + 3)).owner = SNOW;
                            ((MethodInsnNode) method.instructions.get(j + 5)).owner = SNOW;
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
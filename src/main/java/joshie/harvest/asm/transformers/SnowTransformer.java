package joshie.harvest.asm.transformers;

import joshie.harvest.asm.ASMConstants;
import joshie.harvest.asm.ASMHelper;
import joshie.harvest.core.config.ASM;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

public class SnowTransformer extends AbstractASM {
    @Override
    public boolean isActive(ASM config) {
        return config.SNOW_OVERRIDE;
    }

    @Override
    public boolean isClass(String name) {
        return name.equals("aji") || name.equals(ASMConstants.BLOCK);
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
            if ((method.name.equals("registerBlocks") || method.name.equals("func_149671_p")) && method.desc.equals(ASMHelper.toMethodDescriptor("V"))) {
                for (int j = 0; j < method.instructions.size(); j++) {
                    AbstractInsnNode instruction = method.instructions.get(j);
                    if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                        LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                        if (ldcInstruction.cst.equals("SNOW_LAYER")) {
                            ((TypeInsnNode) method.instructions.get(j + 1)).desc = ASMHelper.toInternalClassName(ASMConstants.Overrides.SNOW);
                            ((MethodInsnNode) method.instructions.get(j + 3)).owner = ASMHelper.toInternalClassName(ASMConstants.Overrides.SNOW);
                            ((MethodInsnNode) method.instructions.get(j + 5)).owner = ASMHelper.toInternalClassName(ASMConstants.Overrides.SNOW);
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
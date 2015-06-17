package joshie.harvest.asm.transformers;

import java.util.Iterator;

import joshie.harvest.asm.HFOverride;
import joshie.harvest.core.config.ASM;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class FarmlandHardnessTransformer extends AbstractASM {
    @Override
    public boolean isActive(ASM config) {
        return config.FARMLAND_OVERRIDE;
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
        String name = HFOverride.isObfuscated ? "func_149671_p" : "registerBlocks";
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
                        if (ldcInstruction.cst.equals("wheat")) {
                            ((LdcInsnNode) m.instructions.get(j + 14)).cst = -1.0F;
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

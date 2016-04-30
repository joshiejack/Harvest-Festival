package joshie.harvest.asm.transformers;

import joshie.harvest.asm.ASMConstants;
import joshie.harvest.asm.ASMHelper;
import joshie.harvest.asm.HFOverride;
import joshie.harvest.core.config.ASM;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class FarmlandTransformer extends AbstractASM {
    @Override
    public boolean isActive(ASM config) {
        return config.FARMLAND_OVERRIDE;
    }

    @Override
    public boolean isClass(String name) {
        return name.equals("aky") || name.equals(ASMConstants.FARMLAND);
    }

    @Override
    public boolean isVisitor() {
        return false;
    }

    @Override
    public byte[] transform(byte[] data) {
        String name = HFOverride.isObfuscated ? "func_180650_b" : "updateTick";
        String desc = ASMHelper.toMethodDescriptor("V", ASMConstants.WORLD, ASMConstants.POS, ASMConstants.STATE, ASMConstants.RANDOM);

        ClassNode node = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(node, 0);

        //Remove the instructions from onRightClick for the ItemEgg
        Iterator<MethodNode> methods = node.methods.iterator();
        while (methods.hasNext()) {
            MethodNode m = methods.next();
            if ((m.name.equals(name) && m.desc.equals(desc))) {
                Iterator<AbstractInsnNode> iter = m.instructions.iterator();
                while (iter.hasNext()) {
                    AbstractInsnNode insn = iter.next();
                    m.instructions.remove(insn);
                }

                //return stack
                m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                m.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.FARMLAND), "tick", ASMHelper.toMethodDescriptor("V", ASMConstants.WORLD, ASMConstants.POS, ASMConstants.STATE), false));
                m.instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
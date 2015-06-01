package joshie.harvest.asm.transformers;

import java.util.Iterator;

import joshie.harvest.core.config.Vanilla;
import joshie.harvest.core.lib.HFModInfo;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class FarmlandTransformer implements ITransformer {
    @Override
    public boolean isActive(Vanilla config) {
        return config.FARMLAND_OVERRIDE;
    }

    @Override
    public String getClass(boolean isObfuscated) {
        return isObfuscated ? "aky" : "net.minecraft.block.BlockFarmland";
    }

    @Override
    public byte[] transform(byte[] data, boolean isObfuscated) {
        String name = isObfuscated ? "func_149674_a" : "updateTick";
        String desc = "(Lnet/minecraft/world/World;IIILjava/util/Random;)V";
        
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
                m.instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
                m.instructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
                m.instructions.add(new VarInsnNode(Opcodes.ILOAD, 4));
                m.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/overrides/BlockFarmland", "tick", "(Lnet/minecraft/world/World;III)V", false));
                m.instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
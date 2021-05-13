package uk.joshiejack.energy.asm;

import uk.joshiejack.penguinlib.asm.AbstractASM;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.HashSet;
import java.util.Set;

public class ASMJump extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.entity.player.EntityPlayer") || name.equals("aeb");
    }

    private boolean isValidMethod(String name, String descriptor) {
        return descriptor.equals("()V") && (name.equals("jump") || name.equals("func_70664_aZ ")) || (descriptor.equals("(DDD)V") && (name.equals("addMovementStat") || name.equals("func_71000_j ")));
    }

    private boolean isAddExhaustion(String name, String descriptor) {
        return descriptor.equals("(F)V") && (name.equals("addExhaustion"));
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (isValidMethod(method.name, method.desc)) {
                Set<AbstractInsnNode> remove = new HashSet<>();
                for (int i = 0; i < method.instructions.size(); i++) {
                    AbstractInsnNode node = method.instructions.get(i);
                    if (node instanceof MethodInsnNode) {
                        MethodInsnNode mNode = ((MethodInsnNode) node);
                        if ((isAddExhaustion(mNode.name, mNode.desc))){
                            for (int j = 0; j <= 10; j++) {
                                int index = i - j;
                                if (index >= 0) {
                                    if (method.instructions.get(index) instanceof LineNumberNode) break;
                                    else remove.add(method.instructions.get(index));
                                }
                            }
                        }
                    }
                }

                remove.forEach(i -> method.instructions.remove(i));
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}

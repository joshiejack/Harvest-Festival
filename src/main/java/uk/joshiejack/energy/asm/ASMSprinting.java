package uk.joshiejack.energy.asm;

import uk.joshiejack.penguinlib.asm.AbstractASM;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashSet;
import java.util.Set;

public class ASMSprinting extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.client.entity.EntityPlayerSP") || name.equals("bub");
    }

    private boolean isValidMethod(String name, String descriptor) {
        return (descriptor.equals("()V") && (name.equals("onLivingUpdate") || name.equals("func_70636_d")));
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        finish:
        for (MethodNode method : classNode.methods) {
            if (isValidMethod(method.name, method.desc)) {
                Set<AbstractInsnNode> remove = new HashSet<>();
                for (int i = 0; i < method.instructions.size(); i++) {
                    AbstractInsnNode node = method.instructions.get(i);
                    if (node instanceof LdcInsnNode) {
                        LdcInsnNode ldcNode = ((LdcInsnNode) node);
                        if (ldcNode.cst instanceof Float && ((Float)ldcNode.cst) == 6.0F) {
                            InsnList list = new InsnList();
                            list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/client/entity/EntityPlayerSP", "getFoodStats", "()Lnet/minecraft/util/FoodStats;", false));
                            list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "uk/joshiejack/energy/EnergyFoodStats", "getEnergyRequiredForSprint", "(Lnet/minecraft/util/FoodStats;)F", false));
                            method.instructions.insertBefore(ldcNode, list);
                            method.instructions.remove(ldcNode);
                            break finish;
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

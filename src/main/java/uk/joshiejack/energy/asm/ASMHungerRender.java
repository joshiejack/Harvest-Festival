package uk.joshiejack.energy.asm;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.asm.AbstractASM;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ASMHungerRender extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraftforge.client.GuiIngameForge");
    }

    private boolean isValidMethod(String name, String descriptor) {
        return name.equals("renderFood") && descriptor.equals("(II)V");
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (isValidMethod(method.name, method.desc)) {
                for (int i = 0; i < method.instructions.size(); i++) {
                    AbstractInsnNode node = method.instructions.get(i);
                    if (node instanceof IntInsnNode && ((IntInsnNode)node).operand == 10) {
                        if (method.instructions.get(i - 1) instanceof VarInsnNode) {
                            InsnList list = new InsnList();
                            list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraftforge/client/GuiIngameForge", PenguinLib.IS_DEOBF ? "mc" : "field_73839_d", "Lnet/minecraft/client/Minecraft;"));
                            list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "uk/joshiejack/energy/client/HungerRender", "getMaxFoodForDisplay", "(Lnet/minecraft/client/Minecraft;)I", false));
                            method.instructions.insert(node, list);
                            method.instructions.remove(node);
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

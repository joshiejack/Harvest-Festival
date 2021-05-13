package uk.joshiejack.energy.asm;

import uk.joshiejack.penguinlib.asm.AbstractASM;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ASMAppleSkin extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("squeek.appleskin.client.HUDOverlayHandler");
    }

    private boolean isValidMethod(String name) {
        return name.equals("drawSaturationOverlay") || name.equals("drawHungerOverlay");
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (isValidMethod(method.name)) {
                for (int i = 0; i < method.instructions.size(); i++) {
                    AbstractInsnNode node = method.instructions.get(i);
                    if (node instanceof LdcInsnNode) { //drawSaturationOverlay
                        LdcInsnNode insn = (LdcInsnNode) node;
                        if (insn.cst.equals(20.0F)) {
                            InsnList list = new InsnList();
                            list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "uk/joshiejack/energy/energy/HungerRender", "getMaxFood", "(Lnet/minecraft/client/Minecraft;)I", false));
                            list.add(new InsnNode(Opcodes.I2F));
                            method.instructions.insert(insn, list);
                            method.instructions.remove(insn); //Remove this instruction
                        }
                    } else if (node instanceof IntInsnNode) { //drawHungerOverlay
                        IntInsnNode insn = (IntInsnNode) node;
                        if (insn.getOpcode() == Opcodes.BIPUSH && insn.operand == 20) {
                            InsnList list = new InsnList();
                            list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "uk/joshiejack/energy/energy/HungerRender", "getMaxFood", "(Lnet/minecraft/client/Minecraft;)I", false));
                            method.instructions.insert(insn, list);
                            method.instructions.remove(insn); //Remove this instruction
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

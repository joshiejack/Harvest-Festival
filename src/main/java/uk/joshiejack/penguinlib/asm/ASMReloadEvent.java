package uk.joshiejack.penguinlib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ASMReloadEvent extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.server.MinecraftServer"); //Obf and Deobf names are the same
    }

    private boolean isValidMethod(String name, String description) {
        return (name.equals("reload") || name.equals("func_193031_aM")) && description.equals("()V");
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
                    if (node instanceof JumpInsnNode && method.instructions.get(i - 1) instanceof MethodInsnNode &&
                            ((MethodInsnNode)method.instructions.get(i - 1)).name.equals("reloadResources")) {
                        InsnList list = new InsnList();
                        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
                        list.add(new TypeInsnNode(Opcodes.NEW, "uk/joshiejack/penguinlib/events/ReloadResourcesEvent"));
                        list.add(new InsnNode(Opcodes.DUP));
                        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/MinecraftServer", "worlds", "[Lnet/minecraft/world/WorldServer;"));
                        list.add(new InsnNode(Opcodes.ICONST_0));
                        list.add(new InsnNode(Opcodes.AALOAD));
                        list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "uk/joshiejack/penguinlib/events/ReloadResourcesEvent", "<init>", "(Lnet/minecraft/world/World;)V", false));
                        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
                        list.add(new InsnNode(Opcodes.POP));
                        method.instructions.insert(method.instructions.get(i - 1), list);
                    }

                    //if (node instanceof IntInsnNode && ((IntInsnNode) node).operand == 10) { //TODO ?
                        //if (method.instructions.get(i - 1) instanceof VarInsnNode) {

                            //method.instructions.insert(node, list);
                            //method.instructions.remove(node);
                        //}
                    //}
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}

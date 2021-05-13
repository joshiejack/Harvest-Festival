package uk.joshiejack.seasons.asm;

import uk.joshiejack.penguinlib.asm.AbstractASM;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ASMTemperature extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.world.biome.Biome") || name.equals("anf");
    }

    private boolean isValidMethod(String name, String description) {
        return (name.equals("getTemperature") || name.equals("func_180626_a")) && description.equals("(Lnet/minecraft/util/math/BlockPos;)F");
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (isValidMethod(method.name, method.desc)) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "uk/joshiejack/seasons/Seasons", "getTemperature", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/Biome;)F", false));
                list.add(new InsnNode(Opcodes.FRETURN));
                method.instructions.clear();
                method.instructions.insert(list);
                break; //We're done!
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}

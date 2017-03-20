package joshie.harvest.asm.transformers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

/** Adds a hook to fishing so that fish caught with it's fishing rods can count as having been caught in the fishing collection **/
public class FishingTransformer extends AbstractASM {
    private final String[] names;
    private final String path;
    private int aload;

    public FishingTransformer(int aload, String... names) {
        this.names = names;
        this.aload = aload;
        this.path = names[0].replace(".", "/");
    }

    @Override
    public boolean isClass(String name) {
        for (String n: names)
            if (n.equals(name)) return true;
        return false;
    }

    @Override
    public ClassVisitor newInstance(ClassWriter writer) {
        return new FishingTransformer.Visitor(writer);
    }

    @SuppressWarnings("WeakerAccess")
    public class Visitor extends ClassVisitor {
        public Visitor(ClassWriter writer) {
            super(Opcodes.ASM4, writer);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
            if ((name.equals("handleHookRetraction") || name.equals("func_146034_e") || name.equals("j")) && desc.equals("()I")) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    @Override
                    public void visitTypeInsn(int opcode, String type) {
                        if (opcode == Opcodes.NEW && type.equals("net/minecraft/entity/item/EntityItem")) {
                            mv.visitVarInsn(ALOAD, aload);
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitFieldInsn(GETFIELD, path, "angler", "Lnet/minecraft/entity/player/EntityPlayer;");
                            mv.visitMethodInsn(INVOKESTATIC, "joshie/harvest/fishing/FishingHelper", "track", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;)V", false);
                        }

                        super.visitTypeInsn(opcode, type);
                    }
                };
            }

            return visitor;
        }
    }
}

package joshie.harvest.asm.transformers;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

/** This gives me a hook in to renderByItem so i can do what vanilla does
 *  with my entities, to render them in the inventory, this may seem bad, but before i was simply creating
 *  a bunch of fake tile entities and renderers, so i could do this, which added a lot of bloat,
 *  doing it this way i can pass the renderers the stack **/
public class RenderItemTransformer extends AbstractASM {
    private static final String RENDER_HOOK = "joshie/harvest/asm/RenderHook";

    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer") || name.equals("bqa");
    }

    @Override
    public ClassVisitor newInstance(ClassWriter writer) {
        return new Visitor(writer);
    }

    public class Visitor extends ClassVisitor {
        public Visitor(ClassWriter writer) {
            super(Opcodes.ASM4, writer);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
            if (((name.equals("renderByItem") || name.equals("func_179022_a")) && desc.equals("(Lnet/minecraft/item/ItemStack;)V")) || (name.equals("a") && desc.equals("(Ladz;)V"))) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    @Override
                    public void visitCode() {
                        /* Calls render transformer for the item **/
                        Label l0 = new Label();
                        mv.visitLabel(l0);
                        mv.visitVarInsn(ALOAD, 1);
                        mv.visitMethodInsn(INVOKESTATIC, RENDER_HOOK, "render", "(Lnet/minecraft/item/ItemStack;)Z", false);
                        Label l1 = new Label();
                        mv.visitJumpInsn(IFEQ, l1);
                        mv.visitInsn(RETURN);
                        mv.visitLabel(l1);
                        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                        super.visitCode();
                    }
                };
            }

            return visitor;
        }
    }
}

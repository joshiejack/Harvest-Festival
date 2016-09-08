package joshie.harvest.asm.transformers;

import joshie.harvest.asm.HFTransformer;
import org.objectweb.asm.*;

import static net.minecraftforge.fml.common.Loader.MC_VERSION;
import static org.objectweb.asm.Opcodes.*;

public class RenderRainTransformer extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.client.renderer.EntityRenderer") ||
                (MC_VERSION.equals("1.9.4") && name.equals("bnd")) ||
                (MC_VERSION.equals("1.10.2") && name.equals("bnz"));
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
            if (desc.equals("()V") && (name.equals("addRainParticles") || name.equals("func_78484_h") || name.equals("p"))) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    @Override
                    public void visitCode() {
                        /** Redirects addRainParticles to mine if it's raining, otherwise cancels the effect **/
                        String mc = !HFTransformer.isObfuscated ? "mc" : "field_78531_r";
                        String theWorld = !HFTransformer.isObfuscated ? "theWorld" : "field_71441_e";
                        Label l0 = new Label();
                        mv.visitLabel(l0);
                        mv.visitFieldInsn(GETSTATIC, "joshie/harvest/api/HFApi", "calendar", "Ljoshie/harvest/api/calendar/CalendarManager;");
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", mc, "Lnet/minecraft/client/Minecraft;");
                        mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", theWorld, "Lnet/minecraft/client/multiplayer/WorldClient;");
                        mv.visitMethodInsn(INVOKEINTERFACE, "joshie/harvest/api/calendar/CalendarManager", "getWeather", "(Lnet/minecraft/world/World;)Ljoshie/harvest/api/calendar/Weather;", true);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "joshie/harvest/api/calendar/Weather", "isRain", "()Z", false);
                        Label l1 = new Label();
                        mv.visitJumpInsn(IFEQ, l1);
                        Label l2 = new Label();
                        mv.visitLabel(l2);
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", mc, "Lnet/minecraft/client/Minecraft;");
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "random", "Ljava/util/Random;");
                        mv.visitMethodInsn(INVOKESTATIC, "joshie/harvest/asm/RenderHook", "addRainParticles", "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/renderer/EntityRenderer;Ljava/util/Random;)V", false);
                        mv.visitLabel(l1);
                        mv.visitInsn(RETURN);
                    }
                };
            }
            return visitor;
        }
    }
}
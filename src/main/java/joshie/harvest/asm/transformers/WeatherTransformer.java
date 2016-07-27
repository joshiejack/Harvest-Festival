package joshie.harvest.asm.transformers;

import joshie.harvest.asm.HFTransformer;
import org.objectweb.asm.*;

import static net.minecraftforge.fml.common.Loader.MC_VERSION;
import static org.objectweb.asm.Opcodes.*;

public class WeatherTransformer extends AbstractASM {
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
                        /** Cancels the rendering of rain particles if the weather type returns false on isRain **/
                        Label l0 = new Label();
                        mv.visitLabel(l0);
                        mv.visitVarInsn(ALOAD, 0);
                        String mc = !HFTransformer.isObfuscated ? "mc" : "field_78531_r";
                        mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", mc, "Lnet/minecraft/client/Minecraft;");
                        String theWorld = !HFTransformer.isObfuscated ? "theWorld" : "field_71441_e";
                        mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", theWorld, "Lnet/minecraft/client/multiplayer/WorldClient;");
                        mv.visitMethodInsn(INVOKESTATIC, "joshie/harvest/core/handlers/HFTrackers", "getCalendar", "(Lnet/minecraft/world/World;)Ljoshie/harvest/calendar/Calendar;", false);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "joshie/harvest/calendar/Calendar", "getTodaysWeather", "()Ljoshie/harvest/api/calendar/Weather;", false);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "joshie/harvest/api/calendar/Weather", "isRain", "()Z", false);
                        Label l1 = new Label();
                        mv.visitJumpInsn(IFNE, l1);
                        mv.visitInsn(RETURN);
                        mv.visitLabel(l1);
                        super.visitCode();
                    }
                };
            }
            return visitor;
        }
    }
}
package joshie.harvest.asm.transformers;

import joshie.harvest.asm.ASMHelper;
import joshie.harvest.asm.HFOverride;
import org.objectweb.asm.*;

import static joshie.harvest.asm.ASMConstants.*;
import static joshie.harvest.asm.ASMHelper.toDescriptor;
import static joshie.harvest.asm.ASMHelper.toInternalClassName;
import static org.objectweb.asm.Opcodes.*;

public class WeatherTransformer extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals(ENTITY_RENDERER) || name.equals("bnd");
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
            if (desc.equals(ASMHelper.toMethodDescriptor("V")) && (name.equals("addRainParticles") || name.equals("func_78484_h") || name.equals("p"))) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    @Override
                    public void visitCode() {
                        /** Cancels the rendering of rain particles if the weather type returns false on isRain **/
                        Label l0 = new Label();
                        mv.visitLabel(l0);
                        mv.visitVarInsn(ALOAD, 0);
                        String mc = !HFOverride.isObfuscated ? "mc" : "field_78531_r";
                        mv.visitFieldInsn(GETFIELD, toInternalClassName(ENTITY_RENDERER), mc, toDescriptor(MINECRAFT));
                        String theWorld = !HFOverride.isObfuscated ? "theWorld" : "field_71441_e";
                        mv.visitFieldInsn(GETFIELD, toInternalClassName(MINECRAFT), theWorld, toDescriptor(WORLDCLIENT));
                        mv.visitMethodInsn(INVOKESTATIC, toInternalClassName(HFTRACKER), "getCalendar", "(Lnet/minecraft/world/World;)Ljoshie/harvest/calendar/Calendar;", false);
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
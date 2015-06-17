package joshie.harvest.asm.transformers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class WeatherTransformer extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals("net.minecraft.client.renderer.EntityRenderer") || name.equals("bao");
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
            if (desc.equals("()V") && (name.equals("addRainParticles") || name.equals("func_78484_h"))) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    @Override
                    public void visitCode() {
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "mc", "Lnet/minecraft/client/Minecraft;");
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "random", "Ljava/util/Random;");
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "joshie/harvest/calendar/WeatherRenderer", "addRainParticles", "(Lnet/minecraft/client/Minecraft;Ljava/util/Random;Lnet/minecraft/client/renderer/EntityRenderer;)V", false);
                        mv.visitInsn(Opcodes.RETURN);
                    }
                };
            }

            return visitor;
        }
    }
}
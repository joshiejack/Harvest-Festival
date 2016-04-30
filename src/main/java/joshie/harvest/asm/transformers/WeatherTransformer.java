package joshie.harvest.asm.transformers;

import joshie.harvest.asm.ASMConstants;
import joshie.harvest.asm.ASMHelper;
import joshie.harvest.asm.HFOverride;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class WeatherTransformer extends AbstractASM {
    @Override
    public boolean isClass(String name) {
        return name.equals(ASMConstants.ENTITY_RENDERER) || name.equals("blt");
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
            if (desc.equals(ASMHelper.toMethodDescriptor("V")) && (name.equals("addRainParticles") || name.equals("func_78484_h") || name.equals("l"))) {
                return new MethodVisitor(Opcodes.ASM4, visitor) {
                    @Override
                    public void visitCode() {
                        String mc = !HFOverride.isObfuscated ? "mc" : "field_78531_r";
                        String random = !HFOverride.isObfuscated ? "random" : "field_78537_ab";
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitFieldInsn(Opcodes.GETFIELD, ASMHelper.toInternalClassName(ASMConstants.ENTITY_RENDERER), mc, ASMHelper.toDescriptor(ASMConstants.MINECRAFT));
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitFieldInsn(Opcodes.GETFIELD, ASMHelper.toInternalClassName(ASMConstants.ENTITY_RENDERER), random, ASMHelper.toDescriptor(ASMConstants.RANDOM));
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.WEATHER_RENDERER), "addRainParticles", ASMHelper.toMethodDescriptor("V", ASMConstants.MINECRAFT, ASMConstants.RANDOM, ASMConstants.ENTITY_RENDERER), false);
                        mv.visitInsn(Opcodes.RETURN);
                    }
                };
            }
            return visitor;
        }
    }
}
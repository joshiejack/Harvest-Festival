package joshie.harvest.asm.transformers;

import joshie.harvest.asm.ASMConstants;
import joshie.harvest.asm.ASMHelper;
import joshie.harvest.core.config.ASM;
import org.objectweb.asm.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PamTransformer extends AbstractASM {
    @Override
    public boolean isActive(ASM config) {
        return false; //TODO: Harvestcraft Plugin
        //return config.OVERRIDE_HARVESTCRAFT;
    }

    @Override
    public boolean isClass(String name) {
        return name.equals("com.pam.harvestcraft.ItemPamSeedFood");
    }

    @Override
    public boolean isVisitor() {
        return false;
    }

    private byte[] injectInterfaces(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                Set<String> intf = new HashSet<String>();
                intf.addAll(Arrays.asList(interfaces));
                intf.add(ASMHelper.toInternalClassName(ASMConstants.API.SHIPPABLE));
                intf.add(ASMHelper.toInternalClassName(ASMConstants.API.CROP_PROVIDER));
                super.visit(version, access, name, signature, superName, intf.toArray(new String[0]));
            }
        };

        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    private byte[] injectMethods(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);

        //Sellable Sell Value
        String name = "getSellValue";
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("J", ASMConstants.STACK), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.PAM_SEED_FOOD), name, ASMHelper.toMethodDescriptor("J", ASMConstants.STACK), false);
        mv.visitInsn(Opcodes.LRETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Crop Provider
        name = "getCrop";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("L", ASMConstants.STACK) + ASMHelper.toInternalClassName(ASMConstants.API.ICROP), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.PAM_SEED_FOOD), name, ASMHelper.toMethodDescriptor("L", ASMConstants.STACK) + ASMHelper.toInternalClassName(ASMConstants.API.ICROP), false);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();

        //Get Sub Items
        name = "getSubItems";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("V", ASMConstants.ITEM, ASMConstants.CREATIVE_TABS, ASMConstants.LIST), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 3);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.PAM_SEED_FOOD), name, ASMHelper.toMethodDescriptor("V", ASMConstants.ITEM, ASMConstants.CREATIVE_TABS, ASMConstants.LIST), false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(4, 1);
        mv.visitEnd();

        cr.accept(cw, 0);
        return cw.toByteArray();
    }

    @Override
    public byte[] transform(byte[] data) {
        return (injectMethods(injectInterfaces(data)));
    }
}
package joshie.harvestmoon.asm.transformers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import joshie.harvestmoon.core.config.Vanilla;
import joshie.harvestmoon.core.lib.HMModInfo;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PamTransformer implements ITransformer {
    @Override
    public boolean isActive(Vanilla config) {
        return config.OVERRIDE_HARVESTCRAFT;
    }

    @Override
    public String getClass(boolean isObfuscated) {
        return "com.pam.harvestcraft.ItemPamSeedFood";
    }

    public byte[] injectInterfaces(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                Set<String> intf = new HashSet();
                intf.addAll(Arrays.asList(interfaces));
                intf.add(HMModInfo.ASMPATH + "api/core/IShippable");
                intf.add(HMModInfo.ASMPATH + "api/core/IRateable");
                intf.add(HMModInfo.ASMPATH + "api/crops/ICropProvider");
                super.visit(version, access, name, signature, superName, intf.toArray(new String[0]));
            }
        };

        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public byte[] injectMethods(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);

        //Sellable Sell Value
        String name = "getSellValue";
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/ItemStack;)J", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HMModInfo.ASMPATH + "asm/overrides/ItemPamSeedFood", name, "(Lnet/minecraft/item/ItemStack;)J", false);
        mv.visitInsn(Opcodes.LRETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Rateable Quality
        name = "getRating";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/ItemStack;)I", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HMModInfo.ASMPATH + "asm/overrides/ItemPamSeedFood", name, "(Lnet/minecraft/item/ItemStack;)I", false);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Crop Provider
        name = "getCrop";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/ItemStack;)L" + HMModInfo.ASMPATH + "api/crops/ICrop;", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HMModInfo.ASMPATH + "asm/overrides/ItemPamSeedFood", name, "(Lnet/minecraft/item/ItemStack;)L" + HMModInfo.ASMPATH + "api/crops/ICrop;", false);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();

        //Get Sub Items
        name = "getSubItems";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/Item;Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 3);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HMModInfo.ASMPATH + "asm/overrides/ItemPamSeedFood", name, "(Lnet/minecraft/item/Item;Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(4, 1);
        mv.visitEnd();

        cr.accept(cw, 0);
        return cw.toByteArray();
    }

    @Override
    public byte[] transform(byte[] data, boolean isObfuscated) {
        return (injectMethods(injectInterfaces(data)));
    }
}

package joshie.harvest.asm.transformers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import joshie.harvest.core.config.ASM;
import joshie.harvest.core.lib.HFModInfo;

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

    public byte[] injectInterfaces(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                Set<String> intf = new HashSet();
                intf.addAll(Arrays.asList(interfaces));
                intf.add(HFModInfo.ASMPATH + "api/core/IShippable");
                intf.add(HFModInfo.ASMPATH + "api/crops/ICropProvider");
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
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/overrides/ItemPamSeedFood", name, "(Lnet/minecraft/item/ItemStack;)J", false);
        mv.visitInsn(Opcodes.LRETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Crop Provider
        name = "getCrop";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/ItemStack;)L" + HFModInfo.ASMPATH + "api/crops/ICrop;", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/overrides/ItemPamSeedFood", name, "(Lnet/minecraft/item/ItemStack;)L" + HFModInfo.ASMPATH + "api/crops/ICrop;", false);
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
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/overrides/ItemPamSeedFood", name, "(Lnet/minecraft/item/Item;Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V", false);
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

package joshie.harvest.asm.transformers;

import joshie.harvest.asm.HFOverride;
import joshie.harvest.core.config.ASM;
import joshie.harvest.core.config.HFConfig;
import joshie.harvest.core.lib.HFModInfo;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SeedFoodTransformer extends AbstractASM {
    @Override
    public boolean isActive(ASM config) {
        return config.POTATO_OVERRIDE || config.CARROT_OVERRIDE;
    }

    @Override
    public boolean isClass(String name) {
        return name.equals("adv") || name.equals("net.minecraft.item.ItemSeedFood");
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
                intf.add(HFModInfo.ASMPATH + "api/core/ICreativeSorted");
                super.visit(version, access, name, signature, superName, intf.toArray(new String[0]));
            }
        };

        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public byte[] injectMethods(boolean isObfuscated, byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);

        //Sellable Sell Value
        String name = "getSellValue";
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/ItemStack;)J", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/overrides/ItemSeedFood", name, "(Lnet/minecraft/item/ItemStack;)J", false);
        mv.visitInsn(Opcodes.LRETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Crop Provider
        name = "getCrop";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/ItemStack;)L" + HFModInfo.ASMPATH + "api/crops/ICrop;", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/overrides/ItemSeedFood", name, "(Lnet/minecraft/item/ItemStack;)L" + HFModInfo.ASMPATH + "api/crops/ICrop;", false);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
        
        //Creative Sort
        name = "getSortValue";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/ItemStack;)I", null, null);
        mv.visitCode();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/overrides/ItemSeedFood", name, "()I", false);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        //Display Name Override
        name = isObfuscated? "func_77653_i" : "getItemStackDisplayName";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/ItemStack;)Ljava/lang/String;", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/overrides/ItemSeedFood", "getItemStackDisplayName", "(Lnet/minecraft/item/ItemStack;)Ljava/lang/String;", false);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Get Sub Items
        name = isObfuscated? "func_150895_a" : "getSubItems";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/item/Item;Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 3);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/overrides/ItemSeedFood", "getSubItems", "(Lnet/minecraft/item/Item;Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(4, 1);
        mv.visitEnd(); 

        cr.accept(cw, 0);
        return cw.toByteArray();
    }

    @Override
    public byte[] transform(byte[] data) {
        byte[] modified = injectMethods(HFOverride.isObfuscated, injectInterfaces(data));
        if (!HFConfig.asm.CARROT_POTATO_DISABLE_PLANTING) return modified;
        else {
            String name = HFOverride.isObfuscated ? "func_77648_a" : "onItemUse";
            String desc = "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;IIIIFFF)Z";

            ClassNode node = new ClassNode();
            ClassReader classReader = new ClassReader(modified);
            classReader.accept(node, 0);

            //Remove all Instructions from the onItemUse Method
            Iterator<MethodNode> methods = node.methods.iterator();
            while (methods.hasNext()) {
                MethodNode m = methods.next();
                if ((m.name.equals(name) && m.desc.equals(desc))) {
                    Iterator<AbstractInsnNode> iter = m.instructions.iterator();
                    while (iter.hasNext()) {
                        m.instructions.remove(iter.next());
                    }

                    //Return false
                    m.instructions.add(new InsnNode(Opcodes.ICONST_0));
                    m.instructions.add(new InsnNode(Opcodes.IRETURN));
                }
            }

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(writer);
            return writer.toByteArray();
        }
    }
}

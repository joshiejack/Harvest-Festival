package joshie.harvest.asm.transformers;

import joshie.harvest.asm.ASMConstants;
import joshie.harvest.asm.ASMHelper;
import joshie.harvest.asm.HFOverride;
import joshie.harvest.core.config.ASM;
import joshie.harvest.core.config.HFConfig;
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
        return name.equals("adv") || name.equals(ASMConstants.SEED_FOOD);
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
                intf.add(ASMHelper.toInternalClassName(ASMConstants.API.CREATIVE_SORTED));
                super.visit(version, access, name, signature, superName, intf.toArray(new String[0]));
            }
        };

        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    private byte[] injectMethods(boolean isObfuscated, byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);

        //Sellable Sell Value
        String name = "getSellValue";
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("J", ASMConstants.STACK), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.SEED_FOOD), name, ASMHelper.toMethodDescriptor("J", ASMConstants.STACK), false);
        mv.visitInsn(Opcodes.LRETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Crop Provider
        name = "getCrop";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("L", ASMConstants.STACK) + ASMHelper.toInternalClassName(ASMConstants.API.ICROP), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.SEED_FOOD), name, ASMHelper.toMethodDescriptor("L", ASMConstants.STACK) + ASMHelper.toInternalClassName(ASMConstants.API.ICROP), false);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();

        //Creative Sort
        name = "getSortValue";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("I", ASMConstants.STACK), null, null);
        mv.visitCode();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.SEED_FOOD), name, ASMHelper.toMethodDescriptor("I"), false);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        //Display Name Override
        name = isObfuscated ? "func_77653_i" : "getItemStackDisplayName";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor(ASMConstants.STRING, ASMConstants.STACK), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.SEED_FOOD), "getItemStackDisplayName", ASMHelper.toMethodDescriptor(ASMConstants.STRING, ASMConstants.STACK), false);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Get Sub Items
        name = isObfuscated ? "func_150895_a" : "getSubItems";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("V", ASMConstants.ITEM, ASMConstants.CREATIVE_TABS, ASMConstants.LIST), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 3);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.SEED_FOOD), "getSubItems", ASMHelper.toMethodDescriptor("V", ASMConstants.ITEM, ASMConstants.CREATIVE_TABS, ASMConstants.LIST), false);
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
            String name = HFOverride.isObfuscated ? "func_180614_a" : "onItemUse";
            String desc = ASMHelper.toMethodDescriptor(ASMConstants.ENUM_ACTION_RESULT, ASMConstants.STACK, ASMConstants.PLAYER, ASMConstants.WORLD, ASMConstants.POS, ASMConstants.HAND, ASMConstants.FACING, "F", "F", "F");

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